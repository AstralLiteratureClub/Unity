package bet.astral.unity.managers;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerManager implements Listener, Manager {
	private final Map<UUID, Player> playerMap = new HashMap<>();
	private final Unity unity;

	public PlayerManager(Unity unity) {
		this.unity = unity;
	}

	/**
	 * Returns unity player from cache using from bukkit player's unique id
	 * @param player player
	 * @return unity player
	 */
	@NotNull
	public Player fromBukkit(@NotNull org.bukkit.entity.Player player){
		return playerMap.get(player.getUniqueId());
	}

	/**
	 * Returns cached unity player from cache. If no player is found for given id, null is returned
	 * @param uniqueId uniqueId
	 * @return player
	 */
	@Nullable
	public Player get(@NotNull UUID uniqueId){
		return playerMap.get(uniqueId);
	}

	/**
	 * Gets, or loads and caches given player.
	 * @param uniqueId unique id
	 * @return non null player, completable future
	 */
	@NotNull
	public CompletableFuture<@NotNull Player> getOrLoadAndCache(@NotNull UUID uniqueId) {
		if (playerMap.get(uniqueId) != null) {
			return CompletableFuture.completedFuture(playerMap.get(uniqueId));
		} else {
			return unity.getPlayerDatabase()
							.load(uniqueId)
							.thenApply(player -> {
								if (player == null) {
									player = new Player(uniqueId);
								}
								if (player.getFactionIdIgnoreChecks() != null && !unity.getFactionManager().isLoaded(player.getFactionIdIgnoreChecks())) {
									@org.jetbrains.annotations.Nullable Player finalPlayer = player;
									unity.getFactionDatabase()
											.load(player.getFactionIdIgnoreChecks()).thenAccept(f -> {
												if (f == null) {
													finalPlayer.setFactionId(null);
													return;
												}
												if (!f.isMember(finalPlayer.getUniqueId())) {
													finalPlayer.setFactionId(null);
												}
												unity.getFactionManager().add(f);
											});
								}
								playerMap.put(player.getUniqueId(), player);
								return player;
							});
		}
	}

	/**
	 * Removes given player from the player cache
	 * @param uniqueID uniqueId
	 */
	public void unload(UUID uniqueID) {
		playerMap.remove(uniqueID);
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	private void onLogin(AsyncPlayerPreLoginEvent e) {
		getOrLoadAndCache(e.getUniqueId());
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerQuit(PlayerQuitEvent event) {
		unity.getPlayerDatabase().save(playerMap.get(event.getPlayer().getUniqueId())).thenRun(()->playerMap.remove(event.getPlayer().getUniqueId()));
	}

	@Override
	public void saveAll() {
		playerMap.forEach((uuid, player) -> {
			unity.getPlayerDatabase().save(player);
		});
	}
}
