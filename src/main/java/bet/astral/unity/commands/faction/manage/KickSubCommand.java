package bet.astral.unity.commands.faction.manage;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class KickSubCommand extends UnityCommand {
	public KickSubCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot, "kick", Translations.COMMAND_FACTION_KICK_DESCRIPTION,
				b->
						b.permission(requireFaction("unity.kick").and(Permission.KICK_MEMBERS))
								.senderType(Player.class)
								.argument(MemberParser.memberComponent().name("player"))
								.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("reason"))
								.handler(context->{
									Player sender = context.sender();
									OfflinePlayer player = context.get("player");
									String reason = context.getOrDefault("reason", null);
									Component reasonComponent = reason != null ? LegacyComponentSerializer.legacyAmpersand().deserialize(reason) : null;
									unity()
											.getFactionMethods()
											.kick(sender, player, reasonComponent, null);
								})
		).register();
	}
}
