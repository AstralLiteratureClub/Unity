package bet.astral.unity.messenger;

import bet.astral.messenger.v2.cloud.CaptionMessenger;
import bet.astral.messenger.v2.cloud.CloudMessenger;
import bet.astral.messenger.v2.cloud.paper.locale.CommandSenderLocaleExtractor;
import bet.astral.messenger.v2.paper.PaperMessenger;
import bet.astral.messenger.v2.paper.receiver.PlayerReceiver;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class UnityMessenger extends PaperMessenger implements CloudMessenger, CaptionMessenger<CommandSender> {
	private static final OfflinePlayerReceiver OFFLINE_RECEIVER = new OfflinePlayerReceiver();
	private static final CommandSenderLocaleExtractor localeExtractor = new CommandSenderLocaleExtractor();
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

	public static PlaceholderList placeholders(@NotNull OfflinePlayer player, @Nullable Faction faction) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.getName());
		if (faction != null) {
			placeholders.add("faction", faction.getName());
			placeholders.add("faction_id", faction.getUniqueId().toString());
			placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
			FactionMember member = faction.getMember(player.getUniqueId());
			placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));
		} else {
			placeholders.add("faction", "none");
			placeholders.add("faction_id", "none");
			placeholders.add("faction_first_created", "never");
			placeholders.add("faction_first_joined", "never");
		}
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

	@Override
	public org.incendo.cloud.translations.@NotNull LocaleExtractor<CommandSender> getLocaleExtractor() {
		return localeExtractor;
	}
}
