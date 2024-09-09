package bet.astral.unity.gui.core;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerExceptionHandler {
	void accept(Player player);
}
