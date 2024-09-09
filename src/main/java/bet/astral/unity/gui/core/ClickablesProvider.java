package bet.astral.unity.gui.core;

import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface ClickablesProvider {
	List<ClickableLike> provide(@NotNull Player player, @NotNull CachedData cachedData);
	default void handleException(@NotNull Player player) {}
}
