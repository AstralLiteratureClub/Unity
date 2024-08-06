package bet.astral.unity.data;

import bet.astral.unity.entity.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FactionDatabase {
	CompletableFuture<@Nullable Faction> load(@NotNull UUID faction);
	CompletableFuture<Void> save(@NotNull Faction faction);
	CompletableFuture<@NotNull Faction> update(@Nullable Faction faction, @NotNull UUID uniqueId);
}
