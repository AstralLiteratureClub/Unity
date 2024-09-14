package bet.astral.unity.commands.faction.manage;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class BanSubCommand extends UnityCommand {
	public BanSubCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void registerCommands() {
		command(factionRoot, "ban", Translations.COMMAND_FACTION_BAN_DESCRIPTION,
				b->
						b.permission(requireFaction("unity.ban").and(Permission.BAN_PLAYERS))
								.senderType(Player.class)
								.argument(OfflinePlayerParser.offlinePlayerComponent().name("player"))
								.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("reason"))
								.handler(context->{
									Player sender = context.sender();
									OfflinePlayer player = context.get("player");
									String reason = context.getOrDefault("reason", null);
									Component reasonComponent = reason != null ? LegacyComponentSerializer.legacySection().deserialize(reason) : null;
									unity()
											.getFactionMethods()
											.ban(sender, player, reasonComponent, null);
								})
		).register();
	}
}
