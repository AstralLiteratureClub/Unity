package bet.astral.unity.managers;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FactionManager implements Manager {
	private final Map<UUID, Faction> factionMap = new HashMap<>();
	private final Map<String, UUID> factionNameMap = new HashMap<>();
	private final Set<String> bannedNames = new HashSet<>();
	private final Unity unity;

	public FactionManager(Unity unity) {
		this.unity = unity;
	}

	/**
	 * Returns if the banned factions cache contains given name. True if contains (banned), else false
	 * @param name name to check
	 * @return true if banned, else false
	 */
	public boolean isBanned(@NotNull String name){
		return bannedNames.contains(name.toLowerCase());
	}

	/**
	 * Adds given faction to the faction cache
	 * @param faction faction
	 */
	public void add(Faction faction){
		factionMap.put(faction.getUniqueId(), faction);
		factionNameMap.put(faction.getName().toLowerCase(), faction.getUniqueId());
	}

	/**
	 * Returns faction from cache if there is a faction with given faction name.
	 * @param name faction name
	 * @return faction, else null
	 */
	@Nullable
	public Faction get(@NotNull String name){
		if (factionNameMap.get(name.toLowerCase())==null){
			return null;
		}
		return get(factionNameMap.get(name.toLowerCase()));
	}

	/**
	 * Returns faction from cache if there is a faction with given faction id.
	 * @param faction faction id
	 * @return faction, else null
	 */
	@Nullable
	public Faction get(@NotNull UUID faction){
		return factionMap.get(faction);
	}

	/**
	 * Returns true, if there is a faction with given faction id in the faction cache.
	 * @param faction faction
	 * @return true, if found in cache, else false
	 */
	public boolean isLoaded(@NotNull UUID faction){
		return factionMap.get(faction) != null;
	}

	/**
	 * Returns true, if there is a faction with given faction name in the faction name cache.
	 * @param name name
	 * @return true, if found in cache, else false
	 */
	public boolean isLoaded(@NotNull String name){
		if (factionNameMap.get(name)==null){
			return false;
		}
		return factionMap.get(factionNameMap.get(name)) != null;
	}


	public void unload(UUID id) {
		Faction faction = factionMap.get(id);
		if (faction != null) {
			factionMap.remove(faction.getUniqueId());
			factionNameMap.remove(faction.getName().toLowerCase());
			unity.getTickedManager().unregister(faction);
		}
	}

	/**
	 * Loads given faction from a faction database if faction is not found in the faction cache, else returns cache value
	 * @param faction faction
	 * @return faction
	 */
	@NotNull
	public CompletableFuture<@Nullable Faction> load(UUID faction){
		if (factionMap.get(faction)==null) {
			return unity.getFactionDatabase().load(faction).thenApply(f -> {
				if (f == null) {
					return f;
				}
				factionMap.put(f.getUniqueId(), f);
				factionNameMap.put(f.getName().toLowerCase(), f.getUniqueId());
				unity.getTickedManager().register(f);
				return f;
			});
		} else{
			return CompletableFuture.completedFuture(factionMap.get(faction));
		}
	}

	@Override
	public void saveAll() {
		factionMap.forEach((uuid, faction) -> {
			unity.getFactionDatabase().save(faction);
		});
		unity.getFactionInfoDatabase().save(factionNameMap);
	}

	public void saveCachedNames(){
		unity.getFactionInfoDatabase().save(factionNameMap);
	}

	/**
	 * Returns if the name exists inside the stored names and ids
	 * @param name name
	 * @return true, if exists, else false
	 */
	public CompletableFuture<Boolean> exist(String name) {
		return unity.getFactionInfoDatabase().exists(name);
	}
}
