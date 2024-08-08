package bet.astral.unity.events;

public interface Cancellable {
	void setCancelled(boolean value);
	boolean isCancelled();
}
