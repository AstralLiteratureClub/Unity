package bet.astral.unity.gui.faction.own;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.signman.SignMaterial;
import bet.astral.more4j.tuples.Triplet;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.PlayerGUI;
import bet.astral.unity.gui.prebuilt.confirm.ConfirmGUI;
import bet.astral.unity.gui.prebuilt.confirm.SignConfirmGUI;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;

public class MembersGUI extends FactionBaseGUI {
	private final Map<UUID, UUID> modifyingMembers = new HashMap<>();
	private final ConfirmGUI confirmKickGUI;
	private final ConfirmGUI confirmBanGUI;
	private final PlayerGUI membersGUI;

	public MembersGUI(FactionGUI guiHandler) {
		super(guiHandler);
		membersGUI = new PlayerGUI(
				this,
				Translations.GUI_FACTION_MEMBERS,
				Translations.GUI_BUTTON_MEMBERS_MEMBER_NAME, Translations.GUI_BUTTON_MEMBERS_MEMBER_DESCRIPTION,
				Translations.GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_NAME, Translations.GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_MEMBERS_NEXT_PAGE_NAME, Translations.GUI_BUTTON_MEMBERS_NEXT_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_MEMBERS_RETURN_NAME, Translations.GUI_BUTTON_MEMBERS_RETURN_DESCRIPTION,
				5,
				this::openMemberManage,
				guiHandler::openMainMenu,
				null,
				null
		);

		this.confirmKickGUI = new SignConfirmGUI(this,
				Translations.GUI_FACTION_KICK, Translations.GUI_BUTTON_KICK_CONFIRM_NAME, Translations.GUI_BUTTON_KICK_CONFIRM_LORE,
				Translations.GUI_BUTTON_KICK_CONFIRM_SIGN_NAME, Translations.GUI_BUTTON_KICK_CONFIRM_SIGN_LORE,
				Translations.GUI_BUTTON_KICK_CANCEL_NAME, Translations.GUI_BUTTON_KICK_CANCEL_LORE,
				Translations.GUI_BUTTON_KICK_RETURN_NAME, Translations.GUI_BUTTON_KICK_RETURN_LORE,
				(player) -> {
					UUID kickingId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (kickingId == null) {
						handler.openMainMenu(player);
						return;
					}
					OfflinePlayer player1 = Bukkit.getOfflinePlayer(kickingId);
					unity.getFactionMethods().kick(player, player1, null, this::openMembers);
				},
				new SignGUIBuilder()
						.setMaterial(SignMaterial.BIRCH)
						.setColor(DyeColor.RED)
						.setHandler(() -> List.of((player, result) -> {
							UUID kickingId = modifyingMembers.get(player.getUniqueId());
							modifyingMembers.remove(player.getUniqueId());
							if (kickingId == null) {
								root.openMainMenu(player);
								return;
							}
							OfflinePlayer player1 = Bukkit.getOfflinePlayer(kickingId);
							unity.getFactionMethods().kick(player, player1, Component.join(JoinConfiguration.spaces(), result.getLines()), this::openMembers);
						}))
				,
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						handler.openMainMenu(player);
						return;
					}
					UUID kickingId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (kickingId == null) {
						return;
					}
					openMembers(player);
				},
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						handler.openMainMenu(player);
						return;
					}
					UUID kickingId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (kickingId == null) {
						return;
					}
					openMemberManage(player, Bukkit.getOfflinePlayer(kickingId));
				}, null, player -> modifyingMembers.remove(player.getUniqueId()));

		this.confirmBanGUI = new SignConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CONFIRM_SIGN_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_SIGN_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID kickingId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (kickingId == null) {
						handler.openMainMenu(player);
						return;
					}
					OfflinePlayer player1 = Bukkit.getOfflinePlayer(kickingId);
					unity.getFactionMethods().ban(player, player1, null, this::openMembers);
				},
				new SignGUIBuilder()
						.setMaterial(SignMaterial.BIRCH)
						.setColor(DyeColor.RED)
						.setHandler(() -> List.of((player, result) -> {
							UUID banningId = modifyingMembers.get(player.getUniqueId());
							modifyingMembers.remove(player.getUniqueId());
							if (banningId == null) {
								handler.openMainMenu(player);
								return;
							}
							OfflinePlayer player1 = Bukkit.getOfflinePlayer(banningId);
							unity.getFactionMethods().ban(player, player1, Component.join(JoinConfiguration.spaces(), result.getLines()), this::openMembers);
						}))
				,
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						handler.openMainMenu(player);
						return;
					}
					UUID banningId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (banningId == null) {
						return;
					}
					openMembers(player);
				},
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						handler.openMainMenu(player);
						return;
					}
					UUID banningId = modifyingMembers.get(player.getUniqueId());
					modifyingMembers.remove(player.getUniqueId());
					if (banningId == null) {
						return;
					}
					openMemberManage(player, Bukkit.getOfflinePlayer(banningId));
				},
				null, player -> modifyingMembers.remove(player.getUniqueId()));
	}


	public void openMembers(Player player) {
		openMembers(player, 0);
	}

	public void openMembers(Player player, int page) {
		Faction faction = fetchFaction(player).orElse(null);
		if (faction == null){
			handler.openMainMenu(player);
			return;
		}
		Triplet<List<? extends OfflinePlayer>, Integer, Faction> triplet = Triplet.immutable(faction.getMembersAsOfflinePlayers(), page, faction);
		membersGUI.openData(player, triplet);
	}

	public void openMemberManage(Player player, OfflinePlayer offlinePlayer) {
		Faction faction = fetchFaction(player).orElse(null);
		if (faction == null){
			handler.openMainMenu(player);
			return;
		}

		PlaceholderList placeholders = new PlaceholderList(placeholders(offlinePlayer, faction));

		ClickableBuilder kick = Clickable.builder(Material.IRON_AXE)
				.placeholderGenerator(p -> placeholders).title(Translations.GUI_BUTTON_MEMBER_MANAGE_KICK_NAME).description(Translations.COMMAND_FACTION_KICK_DESCRIPTION)
				.permission((Predicate<Player>) player1 -> {
					FactionMember member = faction.getMember(player1.getUniqueId());
					return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
				})
				.displayIfNoPermissions()
				.actionGeneral((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmKickGUI.open(player, placeholders);
				});
		ClickableBuilder ban = Clickable.builder(Material.DIAMOND_AXE).
				placeholderGenerator(p -> placeholders(p, faction))
				.title(Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_NAME)
				.description(Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_DESCRIPTION)
				.permission((Predicate<Player>) player1 -> {
					FactionMember member = faction.getMember(player1.getUniqueId());
					return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
				})
				.displayIfNoPermissions()
				.actionGeneral((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmBanGUI.open(player, placeholders);
				});

		InventoryGUI.builder(2)
				.messenger(getMessenger())
				.placeholderGenerator(p -> placeholders)
				.title(Translations.GUI_FACTION_MEMBER_MANAGE)
				.background(GUIBackgrounds.MANAGE)
				.addClickable(0, kick)
				.addClickable(1, ban)
				.addClickable(13, Clickable.builder(Material.BARRIER)
						.title(Translations.GUI_BUTTON_MEMBER_MANAGE_RETURN_NAME).description(Translations.GUI_BUTTON_BAN_RETURN_LORE)
						.actionGeneral((clickable, itemStack, player1) -> openMembers(player1)))
				.build()
				.open(player);
	}
}