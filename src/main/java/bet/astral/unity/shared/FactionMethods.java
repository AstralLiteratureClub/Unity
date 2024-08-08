package bet.astral.unity.shared;

import bet.astral.messenger.v2.MessageSender;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.unity.Unity;
import bet.astral.unity.data.FactionDatabase;
import bet.astral.unity.data.PlayerDatabase;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FactionMethods implements MessageSender.Packed {
	private final Unity unity;
	private final FactionManager manager;
	private final FactionDatabase database;
	private final PlayerManager playerManager;
	private final PlayerDatabase playerDatabase;
	private final UnityMessenger messenger;

	public FactionMethods(Unity unity) {
		this.unity = unity;
		this.manager = unity.getFactionManager();
		this.database = unity.getFactionDatabase();
		this.playerManager = unity.getPlayerManager();
		this.playerDatabase = unity.getPlayerDatabase();
		this.messenger = unity.getMessenger();
	}

	@Override
	public Messenger getMessenger() {
		return unity.getMessenger();
	}

	/**
	 * Invites given player to the faction, and handles all messages and
	 * @param player
	 * @param whoToInvite
	 */
	public void invite(@NotNull Player player, @Nullable Player whoToInvite, boolean openMenu){
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());

		if (whoToInvite == null){
			if (openMenu) {
				unity.getGuiHandler()
						.getFactionGUI()
						.getInviteGUI()
						.openInvitable(player);
			}
			return;
		}
		PlaceholderList placeholders = UnityMessenger.placeholders(player, faction);
		placeholders.add("who", whoToInvite.getName());
		faction.invite(whoToInvite.getUniqueId());
		message(whoToInvite, Translations.MESSAGE_FACTION_INVITE_RECEIVE, placeholders);
		message(player, Translations.MESSAGE_FACTION_INVITE_SENT, placeholders);
		message(faction, Translations.BROADCAST_FACTION_INVITE_SENT, placeholders);
		if (openMenu){
			unity.getGuiHandler()
					.getFactionGUI()
					.getInviteGUI()
					.openInvitable(player);
		}
	}

	public void cancelInvite(Player player, @Nullable OfflinePlayer whoToRevoke, boolean openMenu) {
		bet.astral.unity.entity.Player fPlayer = playerManager.fromBukkit(player);
		Faction faction = manager.get(fPlayer.getFactionId());

		if (whoToRevoke == null){
			if (openMenu) {
				unity.getGuiHandler()
						.getFactionGUI()
						.getInviteGUI()
						.openSentInvites(player);
			}
			return;
		}
		PlaceholderList placeholders = UnityMessenger.placeholders(player, faction);
		placeholders.add("who", whoToRevoke.getName());
		faction.cancelInvite(whoToRevoke.getUniqueId());
		message(whoToRevoke, Translations.MESSAGE_FACTION_INVITE_RECEIVE, placeholders);
		message(player, Translations.MESSAGE_FACTION_INVITE_SENT, placeholders);
		message(faction, Translations.BROADCAST_FACTION_INVITE_SENT, placeholders);
		if (openMenu){
			unity.getGuiHandler()
					.getFactionGUI()
					.getInviteGUI()
					.openInvitable(player);
		}
	}
}
