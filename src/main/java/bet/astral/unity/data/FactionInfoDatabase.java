package bet.astral.unity.data;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FactionInfoDatabase {
	CompletableFuture<Boolean> exists(@NotNull UUID uniqueId);
	CompletableFuture<Boolean> exists(@NotNull String name);
	CompletableFuture<Void> save(@NotNull Map<String, UUID> cachedNames);
	CompletableFuture<Collection<UUID>> getAllExistingIds();
	CompletableFuture<Collection<String>> getAllExistingNames();
	CompletableFuture<UUID> getIdFromName(@NotNull String name);
	CompletableFuture<String> getNameFromId(@NotNull UUID uniqueId);
}
