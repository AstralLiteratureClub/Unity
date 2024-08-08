package bet.astral.unity.events;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EventManager {
	private final Map<Class<? extends Event>, Map<EventPriority, Set<EventListener<?>>>> listeners = new HashMap<>();

	public <E extends Event> void register(@NotNull EventListener<E> eventListener){
		register(eventListener, EventPriority.NORMAL);
	}

	public <E extends Event> void register(@NotNull EventListener<E> eventListener, @NotNull EventPriority priority){
		listeners.putIfAbsent(eventListener.getEventType(), new HashMap<>());
		listeners.get(eventListener.getEventType()).putIfAbsent(priority, new HashSet<>());
		listeners.get(eventListener.getEventType()).get(priority).add(eventListener);
	}

	public <E extends Event> CompletableFuture<E> call(@NotNull E event) {
		return CompletableFuture.supplyAsync(()->{
			if (listeners.get(event.getClass())== null){
				return event;
			}
			Map<EventPriority, Set<EventListener<?>>> listeners = this.listeners.get(event.getClass());
			call(listeners, EventPriority.FIRST, event);
			call(listeners, EventPriority.NORMAL, event);
			call(listeners, EventPriority.LAST, event);
			return event;
		});
	}

	@Contract(pure = true)
	private <E extends Event> void call(Map<EventPriority, Set<EventListener<?>>> map, EventPriority priority, E event){
		//noinspection unchecked
		map.get(priority)
				.stream()
				.map(listener->(EventListener<E>) listener)
				.forEach(listener->listener.listen(event));
	}
}
