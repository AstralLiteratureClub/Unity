package bet.astral.unity.gui.faction;

import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.signman.SignMaterial;
import bet.astral.tuples.Triplet;
import bet.astral.unity.commands.faction.manage.BanSubCommand;
import bet.astral.unity.commands.faction.manage.KickSubCommand;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.PlayerGUI;
import bet.astral.unity.gui.prebuilt.confirm.ConfirmGUI;
import bet.astral.unity.gui.prebuilt.confirm.SignConfirmGUI;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
				(player, offlinePlayer) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					openMemberManage(player, offlinePlayer, faction);
				},
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
					KickSubCommand.handle(player, Bukkit.getOfflinePlayer(kickingId), null, true);
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
							KickSubCommand.handle(player, Bukkit.getOfflinePlayer(kickingId), result.getPlainLines(), true);
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
					openMembers(player, faction);
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
					openMemberManage(player, Bukkit.getOfflinePlayer(kickingId), faction);
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
					BanSubCommand.handle(player, Bukkit.getOfflinePlayer(kickingId), null, true);
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
							BanSubCommand.handle(player, Bukkit.getOfflinePlayer(banningId), result.getPlainLines(), true);
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
					openMembers(player, faction);
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
					openMemberManage(player, Bukkit.getOfflinePlayer(banningId), faction);
				},
				null, player -> modifyingMembers.remove(player.getUniqueId()));
	}


	public void openMembers(Player player, Faction faction) {
		openMembers(player, faction, 0);
	}

	public void openMembers(Player player, @NotNull Faction faction, int page) {
		Triplet<List<? extends OfflinePlayer>, Integer, Faction> triplet = Triplet.immutable(faction.getMembersAsOfflinePlayers(), page, faction);
		membersGUI.openData(player, triplet);
	}

	public void openMemberManage(Player player, OfflinePlayer offlinePlayer, Faction faction) {
		PlaceholderList placeholders = new PlaceholderList(placeholders(offlinePlayer, faction));

		ClickableBuilder kick = new ClickableBuilder(Material.IRON_AXE, meta -> {
			meta.displayName(component(player, Translations.GUI_BUTTON_MEMBER_MANAGE_KICK_NAME, placeholders));
			meta.lore(lore(player, Translations.GUI_BUTTON_MEMBER_MANAGE_KICK_DESCRIPTION, placeholders));
		})
				.permission((Predicate<Player>) player1 -> {
					FactionMember member = faction.getMember(player1.getUniqueId());
					return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
				})
				.displayIfNoPermissions()
				.actionGeneral((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmKickGUI.open(player, placeholders);
				});
		ClickableBuilder ban = new ClickableBuilder(Material.DIAMOND_AXE, meta -> {
			meta.displayName(component(player, Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_NAME, placeholders));
			meta.lore(lore(player, Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_DESCRIPTION, placeholders));
		})
				.permission((Predicate<Player>) player1 -> {
					FactionMember member = faction.getMember(player1.getUniqueId());
					return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
				})
				.displayIfNoPermissions()
				.actionGeneral((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmBanGUI.open(player, placeholders);
				});

		new InventoryGUIBuilder(2)
				.messenger(getMessenger())
				.title(component(player, Translations.GUI_FACTION_MEMBER_MANAGE, placeholders))
				.background(GUIBackgrounds.MANAGE)
				.addClickable(0, kick)
				.addClickable(1, ban)
				.addClickable(13, new ClickableBuilder(Material.BARRIER, meta -> {
					meta.displayName(component(player, Translations.GUI_BUTTON_MEMBER_MANAGE_RETURN_NAME));
					meta.lore(lore(player, Translations.GUI_BUTTON_MEMBER_MANAGE_RETURN_DESCRIPTION));
				})
						.actionGeneral((clickable, itemStack, player1) -> openMembers(player1, faction)))
				.build()
				.open(player);
	}
}