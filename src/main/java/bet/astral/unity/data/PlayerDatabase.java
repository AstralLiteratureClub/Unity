package bet.astral.unity.data;

import bet.astral.unity.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerDatabase {
	CompletableFuture<@Nullable Player> load(@NotNull UUID player);
	CompletableFuture<Void> save(@NotNull Player player);
	CompletableFuture<@NotNull Player> update(@NotNull Player player);
}
