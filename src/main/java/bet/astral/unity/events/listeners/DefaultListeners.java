package bet.astral.unity.events.listeners;

import bet.astral.more4j.event.EventManager;
import bet.astral.unity.Unity;
import org.jetbrains.annotations.NotNull;

public class DefaultListeners {
	public static void registerListeners(@NotNull Unity unity){
		EventManager eventManager = unity.getEventManager();
		eventManager.register(new FactionInviteExpireListener(unity));
	}
}
