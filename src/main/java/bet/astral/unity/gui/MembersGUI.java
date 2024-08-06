package bet.astral.unity.gui;

import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.signman.SignMaterial;
import bet.astral.unity.commands.faction.manage.BanSubCommand;
import bet.astral.unity.commands.faction.manage.KickSubCommand;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class MembersGUI extends GUIHandler {
	FactionGUI factionGUI;
	private final Map<UUID, UUID> modifyingMembers = new HashMap<>();
	private final ConfirmGUI confirmKickGUI;
	private final ConfirmGUI confirmBanGUI;
	private final PlayerGUI membersGUI;

	public MembersGUI(FactionGUI guiHandler) {
		super(guiHandler);
		this.factionGUI = guiHandler;
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
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					openMembers(player, faction);
				},
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
						factionGUI.openMainMenu(player);
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
								factionGUI.openMainMenu(player);
								return;
							}
							KickSubCommand.handle(player, Bukkit.getOfflinePlayer(kickingId), result.getPlainLines(), true);
						}))
				,
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						factionGUI.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						factionGUI.openMainMenu(player);
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
						factionGUI.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						factionGUI.openMainMenu(player);
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
						factionGUI.openMainMenu(player);
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
								factionGUI.openMainMenu(player);
								return;
							}
							BanSubCommand.handle(player, Bukkit.getOfflinePlayer(banningId), result.getPlainLines(), true);
						}))
				,
				(player) -> {
					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					if (fPlayer.getFactionId() == null) {
						factionGUI.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						factionGUI.openMainMenu(player);
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
						factionGUI.openMainMenu(player);
						return;
					}
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						factionGUI.openMainMenu(player);
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
		int maxSlots = 45;
		int members = faction.getMembers().size();
		double maxPage = ((double) faction.getMembers().size() / maxSlots);
		InventoryGUIBuilder builder = new InventoryGUIBuilder(5)
				.name(component(player, Translations.GUI_FACTION_MEMBERS, Placeholder.of("page", page + 1), Placeholder.of("pages", (int) maxPage + 1)))
				.setBackground(BACKGROUND_LIGHT)
				.setSlotClickable(36, BACKGROUND_DARK)
				.setSlotClickable(37, BACKGROUND_DARK)
				.setSlotClickable(38, BACKGROUND_DARK)
				.setSlotClickable(39, BACKGROUND_DARK)
				.setSlotClickable(41, BACKGROUND_DARK)
				.setSlotClickable(42, BACKGROUND_DARK)
				.setSlotClickable(43, BACKGROUND_DARK)
				.setSlotClickable(44, BACKGROUND_DARK);
		int from = page == 0 ? 0 : (page - 1) * maxSlots;
		int to = page == 0 ? maxSlots : page * maxSlots;
		boolean lastPage;
		boolean firstPage = page == 0;
		if (to > members) {
			to = members;
			lastPage = true;
		} else {
			lastPage = false;
		}
		List<? extends OfflinePlayer> pageMembers = faction.getMembersAsOfflinePlayers().stream().toList().subList(from, to);
		int i = 0;

		FactionMember member = faction.getMember(player.getUniqueId());
		for (OfflinePlayer offlinePlayer : pageMembers) {
			builder.addSlotClickable(i, new ClickableBuilder(Material.PLAYER_HEAD, meta -> {
						meta.setPlayerProfile(offlinePlayer.getPlayerProfile());
						PlaceholderList placeholders = placeholders(offlinePlayer, faction);
						FactionMember offlineMember = faction.getMember(offlinePlayer.getUniqueId());

						meta.displayName(component(player, Translations.GUI_BUTTON_MEMBERS_MEMBER_NAME, placeholders));
						if (member.getRole().canKick(offlineMember.getRole())) {
							meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_MEMBER_CAN_MODIFY_DESCRIPTION, placeholders));
						} else {
							meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_MEMBER_DESCRIPTION, placeholders));
						}
					}, SkullMeta.class)
							.setGeneralAction((clickable, itemStack, player1) -> openMemberManage(player1, offlinePlayer, faction))
			);
			i++;
		}


		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(Placeholder.of("page", page + 1));
		placeholders.add(Placeholder.of("previous_page", page));
		placeholders.add(Placeholder.of("next_page", page + 2));
		placeholders.add(Placeholder.of("pages", (int) maxPage + 1));
		builder
				.addSlotClickable(36, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, Translations.GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_NAME, placeholders));
							meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_DESCRIPTION, placeholders));
							overrideToolTooltips(meta);
						}).setPriority(10)
								.setPermission(bet.astral.guiman.permission.Permission.of(p -> !firstPage))
								.setDisplayIfNoPermissions(false)
				)
				.setSlotClickable(40, new ClickableBuilder(Material.BARRIER, meta -> {
					meta.displayName(component(player, Translations.GUI_BUTTON_MEMBERS_FACTION_MENU_NAME));
					meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_FACTION_MENU_DESCRIPTION));
				}).setGeneralAction(((clickable, itemStack, player1) -> factionGUI.openFactionMenu(player1))))
				.addSlotClickable(44, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, Translations.GUI_BUTTON_MEMBERS_NEXT_PAGE_NAME, placeholders));
							meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_NEXT_PAGE_DESCRIPTION, placeholders));
							overrideToolTooltips(meta);
						}).setPriority(10)
								.setPermission(bet.astral.guiman.permission.Permission.of(p -> !lastPage))
								.setDisplayIfNoPermissions(false)
				)
				.build().generateInventory(player);
	}

	public void openMemberManage(Player player, OfflinePlayer offlinePlayer, Faction faction) {
		PlaceholderList placeholders = new PlaceholderList(placeholders(offlinePlayer, faction));

		ClickableBuilder kick = new ClickableBuilder(Material.IRON_AXE, meta -> {
			meta.displayName(component(player, Translations.GUI_BUTTON_MEMBER_MANAGE_KICK_NAME, placeholders));
			meta.lore(lore(player, Translations.GUI_BUTTON_MEMBER_MANAGE_KICK_DESCRIPTION, placeholders));
		}).setPermission((Predicate<Player>) player1 -> {
			FactionMember member = faction.getMember(player1.getUniqueId());
			return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
		}).setDisplayIfNoPermissions(true)
				.setGeneralAction((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmKickGUI.open(player, placeholders);
				});
		ClickableBuilder ban = new ClickableBuilder(Material.DIAMOND_AXE, meta -> {
			meta.displayName(component(player, Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_NAME, placeholders));
			meta.lore(lore(player, Translations.GUI_BUTTON_MEMBER_MANAGE_BAN_DESCRIPTION, placeholders));
		}).setPermission((Predicate<Player>) player1 -> {
			FactionMember member = faction.getMember(player1.getUniqueId());
			return (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS));
		}).setDisplayIfNoPermissions(true)
				.setGeneralAction((clickable, itemStack, player1) -> {
					modifyingMembers.put(player1.getUniqueId(), offlinePlayer.getUniqueId());
					confirmBanGUI.open(player, placeholders);
				})
				;

		new InventoryGUIBuilder(1)
				.name(component(player, Translations.GUI_FACTION_MEMBER_MANAGE, placeholders))
				.setBackground(BACKGROUND_DARK)
				.setSlotClickable(1, kick)
				.setSlotClickable(2, ban)
				.setSlotClickable(12, new ClickableBuilder(Material.BARRIER, meta-> {
					meta.displayName(component(player, Translations.GUI_BUTTON_MEMBERS_RETURN_NAME));
					meta.lore(lore(player, Translations.GUI_BUTTON_MEMBERS_RETURN_DESCRIPTION));
				}).setGeneralAction((clickable, itemStack, player1) -> openMembers(player1, faction)))
				.build()
				.generateInventory(player);
	}

	public void openInvite(Player player, Faction faction){
	}
}