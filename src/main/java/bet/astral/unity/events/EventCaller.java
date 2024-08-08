package bet.astral.unity.events;

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
