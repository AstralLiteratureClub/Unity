package bet.astral.unity.events;

import org.jetbrains.annotations.NotNull;

public interface EventListener<E extends Event> {
	void listen(@NotNull E event);
	@NotNull
	Class<E> getEventType();
}
