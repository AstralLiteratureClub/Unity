package bet.astral.unity.events;

import bet.astral.more4j.event.Event;
import bet.astral.more4j.event.EventManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface EventCaller {
	@NotNull
	EventManager getEventManager();
	@NotNull
	default <E extends Event> CompletableFuture<E> callEvent(@NotNull E event) {
		return getEventManager().call(event);
	}
}
