package bet.astral.unity.gui.faction;

import bet.astral.tuples.Triplet;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.prebuilt.PlayerGUI;
import bet.astral.unity.gui.prebuilt.confirm.ConfirmGUI;
import bet.astral.unity.messenger.Translations;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InviteGUI extends FactionBaseGUI {
	private final PlayerGUI invitePlayerGUI;
	private final PlayerGUI invitedPlayersGUI;
	private final ConfirmGUI confirmCancelGUI;
	private final ConfirmGUI confirmInviteGUI;
	private final Map<UUID, UUID> confirm = new HashMap<>();
	public InviteGUI(FactionGUI gui) {
		super(gui);

		this.confirmCancelGUI = new ConfirmGUI(this,
				Translations.GUI_FACTION_BAN, Translations.GUI_BUTTON_BAN_CONFIRM_NAME, Translations.GUI_BUTTON_BAN_CONFIRM_LORE,
				Translations.GUI_BUTTON_BAN_CANCEL_NAME, Translations.GUI_BUTTON_BAN_CANCEL_LORE,
				Translations.GUI_BUTTON_BAN_RETURN_NAME, Translations.GUI_BUTTON_BAN_RETURN_LORE,
				(player) -> {
					UUID id = confirm.get(player.getUniqueId());
					confirm.remove(player.getUniqueId());
					if (id == null) {
						openSentInvites(player);
						return;
					}
					unity.getFactionMethods()
									.cancelInvite(player, Bukkit.getOfflinePlayer(id), true);
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
					openSentInvites(player);
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
					openSentInvites(player);
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
						openSentInvites(player);
						return;
					}
					unity.getFactionMethods()
							.invite(player, Bukkit.getPlayer(id), true);
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
					openSentInvites(player);
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
					openSentInvites(player);
				},
				null, player -> confirm.remove(player.getUniqueId()));
		invitedPlayersGUI = new PlayerGUI(
				this,
				Translations.GUI_FACTION_INVITE,
				Translations.GUI_BUTTON_INVITE_PLAYER_NAME, Translations.GUI_BUTTON_INVITE_PLAYER_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_PREVIOUS_PAGE_NAME, Translations.GUI_BUTTON_INVITE_PREVIOUS_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_NEXT_PAGE_NAME, Translations.GUI_BUTTON_INVITE_NEXT_PAGE_DESCRIPTION,
				Translations.GUI_BUTTON_INVITE_RETURN_NAME, Translations.GUI_BUTTON_INVITE_RETURN_DESCRIPTION,
				5,
				(player, offlinePlayer) -> {
					confirm.put(player.getUniqueId(), offlinePlayer.getUniqueId());
					confirmCancelGUI.open(player);
				},
				handler::openMainMenu,
				null,
				null
		);

		invitePlayerGUI = new PlayerGUI(
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

					bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
					Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
					openInviteConfirm(player, invitedPlayer);
				},
				handler::openMainMenu,
				null,
				null
		);
	}

	public void openInvitable(Player player){
		Optional<Faction> faction = fetchFaction(player);
		if (faction.isEmpty()){
			handler.openMainMenu(player);
			return;
		}

		List<? extends Player> players = new LinkedList<>(Bukkit.getOnlinePlayers());
		List<? extends OfflinePlayer> members = faction.get().getMembersAsOfflinePlayers();
		players.removeIf(members::contains);

		Triplet<List<? extends OfflinePlayer>, Integer, Faction> triplet = Triplet.immutable(players, 0, faction.get());
		invitePlayerGUI.openData(player, triplet);
	}

	public void openInviteConfirm(@NotNull Player player, @NotNull Player invited){

	}

	public void openSentInvites(@NotNull Player player){
		Optional<Faction> faction = fetchFaction(player);
		if (faction.isEmpty()){
			handler.openMainMenu(player);
			return;
		}

		List<? extends Player> players = new LinkedList<>(Bukkit.getOnlinePlayers());
		List<? extends OfflinePlayer> members = faction.get().getMembersAsOfflinePlayers();
		players.removeIf(members::contains);

		Triplet<List<? extends OfflinePlayer>, Integer, Faction> triplet = Triplet.immutable(players, 0, faction.get());
		invitedPlayersGUI.openData(player, triplet);
	}
}
