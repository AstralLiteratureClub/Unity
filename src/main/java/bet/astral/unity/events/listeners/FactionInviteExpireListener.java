package bet.astral.unity.events.listeners;

import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.more4j.event.EventListener;
import bet.astral.more4j.event.EventPriority;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.events.faction.FactionInviteExpireEvent;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FactionInviteExpireListener implements EventListener<FactionInviteExpireEvent> {
	private final Unity unity;

	@Contract(pure = true)
	public FactionInviteExpireListener(Unity unity) {
		this.unity = unity;
	}

	@Override
	public void listen(@NotNull FactionInviteExpireEvent event) {
		Faction faction = event.getFaction();
		OfflinePlayer player = Bukkit.getOfflinePlayer(event.getInvite().getUniqueId());

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(UnityMessenger.placeholders(player, faction));
		unity.message(player, Translations.MESSAGE_FACTION_INVITE_EXPIRE, placeholders);
		unity.message(faction, Translations.BROADCAST_FACTION_INVITE_EXPIRE, placeholders);
	}

	@Override
	public @NotNull Class<FactionInviteExpireEvent> getEventType() {
		return FactionInviteExpireEvent.class;
	}

	@Override
	public @NotNull EventPriority getPriority() {
		return EventPriority.FIRST;
	}
}
