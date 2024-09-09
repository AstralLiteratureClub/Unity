package bet.astral.unity.gui.faction.own;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.more4j.tuples.Pair;
import bet.astral.more4j.tuples.Triplet;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.signman.SignMaterial;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.core.data.PlayerCachedData;
import bet.astral.unity.gui.prebuilt.ManagePlayerGUI;
import bet.astral.unity.gui.prebuilt.PlayerGUI;
import bet.astral.unity.gui.prebuilt.confirm.ConfirmGUI;
import bet.astral.unity.gui.prebuilt.confirm.SignConfirmGUI;
import bet.astral.unity.messenger.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NonMembersGUI extends FactionBaseGUI {
	private final PlayerGUI playerGUI;
	private final ConfirmGUI confirmCancelGUI;
	private final ConfirmGUI confirmInviteGUI;
	private final ConfirmGUI confirmBanGUI;
	private final ConfirmGUI confirmUnbanGUI;
	private final ManagePlayerGUI managePlayerGUI;
	private final Map<UUID, UUID> confirm = new HashMap<>();
	public NonMembersGUI(FactionGUI factionGUI) {
		super(factionGUI);

		managePlayerGUI = new ManagePlayerGUI(
				this,
				Translations.GUI_FACTION_MANAGE_NON_MEMBER_TITLE,
				Translations.GUI_FACTION_MANAGE_NON_MEMBER_RETURN_TITLE, Translations.GUI_FACTION_MANAGE_NON_MEMBER_RETURN_DESCRIPTION,
				ChestRows.TWO,
				GUIBackgrounds.MANAGE,
				Set.of(
						Pair.immutable(0,
								(player, cachedData) -> {
									PlayerCachedData playerCachedData = (PlayerCachedData) cachedData;
									Faction faction = NonMembersGUI.this.fetchFaction(player).orElse(null);
									if (faction == null) {
										throw new NullPointerException("Player's current faction is null.");
									}
									PlaceholderList placeholders = new PlaceholderList();
									placeholders.add("player", Objects.requireNonNull(playerCachedData.getManaging().getName()));
									return List.of(
											Clickable.builder(Material.WRITABLE_BOOK)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_ONLINE_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_ONLINE_DESCRIPTION)
													.permission(p -> !faction.isInvited(playerCachedData.getManagingId()) && playerCachedData.getManaging().isOnline())
													.actionGeneral((clickable, itemStack, player1) ->openInviteConfirm(player, (Player) playerCachedData.getManaging()))
													.placeholderGenerator(p->placeholders),
											Clickable.builder(Material.KNOWLEDGE_BOOK)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_OFFLINE_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_OFFLINE_DESCRIPTION)
													.permission(p -> !faction.isInvited(playerCachedData.getManagingId()) && !playerCachedData.getManaging().isOnline())
													.actionGeneral((clickable, itemStack, player1) ->message(player1, Translations.MESSAGE_FACTION_INVITE_NOT_ONLINE, placeholders))
													.placeholderGenerator(p->placeholders),
											Clickable.builder(Material.WRITTEN_BOOK)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_CANCEL_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_INVITE_CANCEL_DESCRIPTION)
													.permission(p -> faction.isInvited(playerCachedData.getManagingId()))
													.actionGeneral((clickable, itemStack, player1) -> openInviteCancel(player, playerCachedData.getManaging()))
													.placeholderGenerator(p->placeholders)
									);
								}
						),
						Pair.immutable(1,
								((player, cachedData) -> {
									PlayerCachedData playerCachedData = (PlayerCachedData) cachedData;
									Faction faction = fetchFaction(player).orElse(null);
									if (faction == null){
										throw new NullPointerException("Player's current faction is null");
									}

									PlaceholderList placeholders = new PlaceholderList();
									placeholders.add("player", Objects.requireNonNull(playerCachedData.getManaging().getName()));

									return List.of(
											Clickable.builder(Material.WOODEN_AXE)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_BAN_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_BAN_DESCRIPTION)
													.permission(p -> !faction.isBanned(playerCachedData.getManagingId()))
													.actionGeneral((clickable, itemStack, player1) -> openBanConfirm(player, playerCachedData.getManaging()))
													.placeholderGenerator(p->placeholders),
											Clickable.builder(Material.DIAMOND_AXE)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_DESCRIPTION)
													.permission(p -> faction.isBanned(playerCachedData.getManagingId()))
													.actionGeneral((clickable, itemStack, player1) -> openUnbanConfirm(player, playerCachedData.getManaging()))
													.placeholderGenerator(p->placeholders)
									);
								})
								),
						Pair.immutable(2,
								((player, cachedData) -> {
									PlayerCachedData playerCachedData = (PlayerCachedData) cachedData;
									Faction faction = fetchFaction(player).orElse(null);
									if (faction == null){
										throw new NullPointerException("Player's current faction is null");
									}

									PlaceholderList placeholders = new PlaceholderList();
									placeholders.add("player", Objects.requireNonNull(playerCachedData.getManaging().getName()));


									bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().get(player.getUniqueId());
									Faction otherFaction = null;
									ItemStack factionBanner = ItemStack.of(Material.AIR);
									if (fPlayer != null){
										otherFaction = unity.getFactionManager().get(fPlayer.getFactionId());
									}

									final Faction finalOtherFaction = otherFaction;
									return List.of(
											Clickable.builder(Material.WHITE_BANNER)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_BAN_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_BAN_DESCRIPTION)
													.permission(p -> finalOtherFaction != null && !finalOtherFaction.isBanned(playerCachedData.getManagingId()))
													.actionGeneral((clickable, itemStack, player1) -> player1.sendMessage("Hey"))
													.placeholderGenerator(p->placeholders),
											Clickable.builder(Material.DIAMOND_AXE)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_DESCRIPTION)
													.permission(p -> finalOtherFaction != null && finalOtherFaction.isBanned(playerCachedData.getManagingId()))
													.actionGeneral((clickable, itemStack, player1) -> player1.sendMessage("Hey 2"))
													.placeholderGenerator(p->placeholders),
											Clickable.builder(Material.BARRIER)
													.hideItemFlags()
													.title(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_TITLE)
													.description(Translations.GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_DESCRIPTION)
													.permission(p -> finalOtherFaction == null)
													.actionGeneral((clickable, itemStack, player1) -> player1.sendMessage("Hey 3"))
													.placeholderGenerator(p->placeholders)
									);
								})
						)
				),
				(p, o)->openPlayers(p),
				null,
				null,
				handler::openMainMenu
		);

		this.confirmUnbanGUI = new ConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = fetchFaction(player).orElse(null);
					if (faction == null){
						handler.openMainMenu(player);
						return;
					}
					unity.getFactionMethods()
							.ban(player, Bukkit.getOfflinePlayer(id),
									null, this::openPlayers);
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					openPlayerManage(player, Bukkit.getOfflinePlayer(id));
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					Player p = Bukkit.getPlayer(id);
					if (p == null)  {
						handler.openMainMenu(player);
						return;
					}
					openPlayerManage(player, p);
				},
				null, player -> confirm.remove(player.getUniqueId()));

		this.confirmBanGUI = new SignConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CONFIRM_SIGN_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_SIGN_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						handler.openMainMenu(player);
						return;
					}
					Faction faction = fetchFaction(player).orElse(null);
					if (faction == null){
						handler.openMainMenu(player);
						return;
					}
					unity.getFactionMethods()
									.ban(player, Bukkit.getOfflinePlayer(id),
											null, this::openPlayers);
				},
				new SignGUIBuilder()
						.setMaterial(SignMaterial.BIRCH)
						.setColor(DyeColor.RED)
						.setHandler(() -> List.of((player, result) -> {
							UUID id = confirm.get(player.getUniqueId());
							confirm.remove(player.getUniqueId());
							if (id == null) {
								handler.openMainMenu(player);
								return;
							}
							Faction faction = fetchFaction(player).orElse(null);
							if (faction == null){
								handler.openMainMenu(player);
								return;
							}
							unity.getFactionMethods()
									.ban(player, Bukkit.getOfflinePlayer(id),
											Component.join(JoinConfiguration.separator(Component.space()), result.getLines()),
											this::openPlayers);
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					openPlayerManage(player, Bukkit.getOfflinePlayer(id));
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					Player p = Bukkit.getPlayer(id);
					if (p == null)  {
						handler.openMainMenu(player);
						return;
					}
					openPlayerManage(player, p);
				},
				null, player -> confirm.remove(player.getUniqueId()));

		this.confirmCancelGUI = new ConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						openPlayers(player);
						return;
					}
					unity.getFactionMethods()
							.cancelInvite(player, Bukkit.getOfflinePlayer(id),
									p -> {
										Player player1 = Bukkit.getPlayer(id);
										if (player1 == null) {
											handler.openMainMenu(player);
											return;
										}
										openPlayerManage(player, player1);
									});
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					openPlayerManage(player, Bukkit.getOfflinePlayer(id));
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
					UUID banningId = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (banningId == null) {
						return;
					}
					openPlayers(player);
				},
				null, player -> confirm.remove(player.getUniqueId()));
		this.confirmInviteGUI = new ConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						openPlayers(player);
						return;
					}
					unity.getFactionMethods()
							.invite(player, Bukkit.getPlayer(id),
									p -> {
										Player player1 = Bukkit.getPlayer(id);
										if (player1 == null) {
											handler.openMainMenu(player);
											return;
										}
										openPlayerManage(player, player1);
									});
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
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						return;
					}
					openPlayerManage(player, Bukkit.getOfflinePlayer(id));
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
					UUID banningId = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (banningId == null) {
						return;
					}
					openPlayers(player);
				},
				null, player -> confirm.remove(player.getUniqueId()));

		playerGUI = new PlayerGUI(
				this,
				Translations.GUI_FACTION_INVITE,
				Translations.GUI_BUTTON_INVITE_PLAYER_NAME, Translations.GUI_BUTTON_INVITE_PLAYER_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_PREVIOUS_PAGE_NAME, Translations.GUI_BUTTON_INVITE_PREVIOUS_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_NEXT_PAGE_NAME, Translations.GUI_BUTTON_INVITE_NEXT_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_RETURN_NAME, Translations.GUI_BUTTON_INVITE_RETURN_DESCRIPTION,
				5,
				(player, invited) -> {
					if (!invited.isOnline()){
						return;
					}

					Player invitedPlayer = invited.getPlayer();
					if (invitedPlayer == null){
						openPlayers(player);
						return;
					}
					openPlayerManage(player, invitedPlayer);
				},
				handler::openMainMenu,
				null,
				null
		);
	}

	private void openBanConfirm(Player player, OfflinePlayer o) {
		Optional<Faction> faction = fetchFaction(player);
		if (faction.isEmpty()){
			handler.openMainMenu(player);
			return;
		}
		confirm.put(player.getUniqueId(), o.getUniqueId());

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", Objects.requireNonNull(o.getName()));
		confirmBanGUI.open(player, placeholders);
	}

	private void openUnbanConfirm(Player player, OfflinePlayer o) {
		Optional<Faction> faction = fetchFaction(player);
		if (faction.isEmpty()){
			handler.openMainMenu(player);
			return;
		}
		confirm.put(player.getUniqueId(), o.getUniqueId());

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", Objects.requireNonNull(o.getName()));
		confirmUnbanGUI.open(player, placeholders);
	}

	public void openPlayers(Player player){
		Optional<Faction> faction = fetchFaction(player);
		if (faction.isEmpty()){
			handler.openMainMenu(player);
			return;
		}

		List<? extends Player> players = new LinkedList<>(Bukkit.getOnlinePlayers());
		List<? extends OfflinePlayer> members = faction.get().getMembersAsOfflinePlayers();
		players.removeIf(members::contains);

		Triplet<List<? extends OfflinePlayer>, Integer, Faction> triplet = Triplet.immutable(players, 0, faction.get());
		playerGUI.openData(player, triplet);
	}

	public void openPlayerManage(Player player, @NotNull OfflinePlayer offlinePlayer){
		Faction faction = fetchFaction(player).orElse(null);
		if (faction == null){
			root.openMainMenu(player);
			return;
		}
		confirm.put(player.getUniqueId(), offlinePlayer.getUniqueId());

		PlaceholderList placeholders = new PlaceholderList();
		managePlayerGUI.openData(player, offlinePlayer, placeholders);
	}

	public void openInviteConfirm(@NotNull Player player, @NotNull Player invited){
		Faction faction = fetchFaction(player).orElse(null);
		if (faction == null){
			root.openMainMenu(player);
			return;
		}
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders(player, faction));
		placeholders.add("who", invited.name());
		confirm.put(player.getUniqueId(), invited.getUniqueId());
		confirmInviteGUI
				.open(player, placeholders);
	}
	public void openInviteCancel(@NotNull Player player, @NotNull OfflinePlayer invited){
		Faction faction = fetchFaction(player).orElse(null);
		if (faction == null){
			root.openMainMenu(player);
		}
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders(player, faction));
		placeholders.add("who", Objects.requireNonNull(invited.getName()));
		confirm.put(player.getUniqueId(), invited.getUniqueId());
		confirmCancelGUI
				.open(player, placeholders);
	}
}
