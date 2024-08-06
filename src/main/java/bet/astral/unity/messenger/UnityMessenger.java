package bet.astral.unity.messenger;

import bet.astral.messenger.v2.paper.PaperMessenger;
import bet.astral.messenger.v2.paper.receiver.PlayerReceiver;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

public class UnityMessenger extends PaperMessenger {
	private static final OfflinePlayerReceiver OFFLINE_RECEIVER = new OfflinePlayerReceiver();
	private Logger logger;
	public UnityMessenger() {
		super(null);
		registerReceiverConverter(o->{
			if (o instanceof Faction) {
				return (Faction) o;
			} else if (o instanceof FactionMember){
				return (FactionMember) o;
			} else if (o instanceof OfflinePlayer && !(o instanceof Player)) {
				return OFFLINE_RECEIVER;
			}
			return null;
		});
	}

	public static PlaceholderList placeholders(Player player, Faction faction) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.name());
		placeholders.add("faction", faction.getName());
		placeholders.add("faction_id", faction.getUniqueId().toString());
		placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
		FactionMember member = faction.getMember(player.getUniqueId());
		placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));

		return placeholders;
	}
	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger of this messenger
	 * @param logger logger
	 * @return this
	 */
	public UnityMessenger setLogger(Logger logger) {
		this.logger = logger;
		return this;
	}

	/**
	 * Converts bukkit player to player receiver
	 * @param player bukkit player
	 * @return messenger paper player
	 */
	public PlayerReceiver asReceiver(Player player){
		return playerManager.players.get(player.getUniqueId());
	}
}
