package bet.astral.unity.commands.faction.manage;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static bet.astral.unity.messenger.UnityMessenger.placeholders;

@Cloud
public class KickSubCommand extends UnityCommand {
	public KickSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot, "kick", Translations.COMMAND_FACTION_KICK_DESCRIPTION,
				b->
						b.permission(requireFaction("unity.kick"))
								.senderType(Player.class)
								.argument(MemberParser.memberComponent().name("player"))
								.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("reason"))
								.handler(context->{
									Player sender = context.sender();
									OfflinePlayer player = context.get("player");
									String reason = context.getOrDefault("reason", null);
									handle(sender, player, reason != null ? List.of(reason) : null, false);
								})
		);
	}

	@Contract(pure = true)
	public static void handle(@NotNull Player player, @NotNull OfflinePlayer kicking, @Nullable List<String> lines, boolean openMenu){
		if (lines == null){
			lines = new LinkedList<>();
			lines.add("no reason given");
		}
		bet.astral.unity.entity.Player fPlayer = unity().getPlayerManager().fromBukkit(player);
		if (fPlayer.getFactionId() == null) {
			if (openMenu)
				unity().getFactionGUI().openMainMenu(player);
			return;
		}
		Faction faction = unity().getFactionManager().get(fPlayer.getFactionId());
		if (faction == null) {
			if (openMenu) unity().getFactionGUI().openMainMenu(player);
			return;
		}
		UUID kickingId = kicking.getUniqueId();
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders(player, faction));
		placeholders.add("who", kicking.getName());
		placeholders.add("reason", String.join (" ", lines));

		FactionMember member = faction.getMember(player.getUniqueId());
		FactionMember kickingMember = faction.getMember(kickingId);
		if (kickingMember == null) {
			if (openMenu) {
				unity().getFactionGUI()
						.getMembersGUI()
						.openMembers(player, faction);
			}
			unity().message(player, Translations.MESSAGE_PLAYER_PLAYER_IS_NOT_A_CLAN_MEMBER, placeholders);
			return;
		}
		if (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS)) {
			if (!member.getRole().canKick(kickingMember.getRole())) {
				unity().message(player, Translations.MESSAGE_FACTION_KICK_CANNOT_KICK_USER, placeholders);
				if (openMenu)
					unity().getFactionGUI()
							.getMembersGUI()
							.openMemberManage(player, Bukkit.getOfflinePlayer(kickingId), faction);
				return;
			}

			faction.getMembers().remove(kickingId);
			unity().getFactionDatabase().save(faction);
			unity().getPlayerManager().getOrLoadAndCache(kickingId)
					.thenAccept(p -> {
						p.setFactionId(null);
						unity().getPlayerDatabase().save(p);
						if (!kicking.isOnline()) {
							unity().getPlayerManager().unload(kickingId);
						}
					});

			unity().message(player, Translations.MESSAGE_FACTION_KICK_KICKED, placeholders);
			unity().message(faction, Translations.BROADCAST_FACTION_KICK_KICKED, placeholders);
			unity().message(kicking, Translations.MESSAGE_FACTION_KICK_WHO_WAS_KICKED, placeholders);
		} else {
			unity().message(player, Translations.MESSAGE_FACTION_KICK_NO_PERMISSIONS, placeholders);
			if (openMenu)
				unity().getFactionGUI()
						.getMembersGUI()
						.openMemberManage(player, Bukkit.getOfflinePlayer(kickingId), faction);
		}
	}

	public static void handleComponents(@NotNull Player player, @NotNull OfflinePlayer offlinePlayer, @NotNull List<? extends Component> lines, boolean openMenu){
		List<String> texts = new ArrayList<>();
		PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

		for (Component component : lines){
			texts.add(plainTextComponentSerializer.serialize(component));
		}
		handle(player, offlinePlayer, texts, openMenu);
	}
}
