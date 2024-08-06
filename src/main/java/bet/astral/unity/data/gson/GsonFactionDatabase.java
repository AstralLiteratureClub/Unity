package bet.astral.unity.data.gson;

import bet.astral.unity.Unity;
import bet.astral.unity.data.FactionDatabase;
import bet.astral.unity.data.FactionInfoDatabase;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.entity.FactionRole;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GsonFactionDatabase implements FactionDatabase, FactionInfoDatabase {
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	private final Unity unity;
	private final Logger logger;

	public GsonFactionDatabase(Unity unity) {
		this.unity = unity;
		logger = unity.getComponentLogger();
	}

	@Override
	public CompletableFuture<@Nullable Faction> load(@NotNull UUID faction) {
		return update(null, faction);
	}

	@Override
	public CompletableFuture<Void> save(@NotNull Faction faction) {
		return CompletableFuture.runAsync(() -> {
			JsonObject data = new JsonObject();
			data.addProperty("uniqueId", faction.getUniqueId().toString());
			data.addProperty("name", faction.getName());
			data.addProperty("firstCreated", faction.getFirstCreated());
			write(data, getDataFile(faction.getUniqueId()));
			if (getUserFolder(faction.getUniqueId()).exists()) {
				getUserFolder(faction.getUniqueId()).delete();
			}
			for (FactionMember member : faction.getMembers().values()){
				JsonObject user = new JsonObject();
				user.addProperty("uniqueId", member.getUniqueId().toString());
				user.addProperty("role", member.getRole().getUniqueId().toString());
				user.addProperty("firstJoined", member.getFirstJoined());
				try {
					write(user, createIfNotExists(getUserFile(faction.getUniqueId(), member.getUniqueId())));
				} catch (IOException e) {
					logger.error("Encountered an exception while trying to get or create a faction data file.", e);
					return;
				}
			}
		});
	}

	@Override
	public CompletableFuture<@NotNull Faction> update(Faction givenFaction, @NotNull UUID factionId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				System.out.println(factionId);
				Faction faction = givenFaction != null ? givenFaction : unity.getFactionManager().get(factionId);
				JsonObject data;
				if (faction == null) {
					data = read(getDataFile(factionId));
					// TODO DATA IS NULL
					if (data == null) {
						return null;
					}
						long firstCreated = data.get("firstCreated") != null ? data.get("firstCreated").getAsLong() : System.currentTimeMillis();
						faction = new Faction(factionId, firstCreated);
				} else {
					data = read(getDataFile(faction.getUniqueId()));
				}
				if (data == null) {
					return faction;
				}

				faction.setName(data.get("name").getAsString());

				for (File file : Objects.requireNonNull(getUserFolder(faction.getUniqueId()).listFiles())) {
					UUID uniqueId = UUID.fromString(file.getName().split("\\.")[0]);
					JsonObject user = read(file);

					long firstJoined = user.get("firstCreated") != null ? user.get("firstCreated").getAsLong() : faction.getFirstCreated();
					FactionMember member = new FactionMember(unity.getMessenger(), uniqueId, firstJoined);
					FactionRole role = FactionRole.fromId(UUID.fromString(user.get("role").getAsString()));
					member.setRole(role);
					faction.getMembers().put(member.getUniqueId(), member);
				}

				File banned = getBannedFile(factionId);
				if (banned != null && banned.exists()){
					JsonArray bannedArray = read(banned, JsonArray.class);
					for (JsonElement ban : bannedArray){
						if (ban.isJsonObject()){
							Object obj = ban.getAsJsonObject();

						}
					}
				}
				return faction;
			} catch (Exception e) {
				logger.error("Encountered exception while trying to load " + factionId, e);
			}
			return null;
		});
	}

	public JsonObject read(File file){
		return read(file, JsonObject.class);
	}

	public <T> T read(File file, Class<T> clazz){
		if (!file.exists()){
			return null;
		}
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			logger.error("Encountered exception while trying to read from file "+ file, e);
			return null;
		}

		T object = gson.fromJson(reader, clazz);

		try {
			reader.close();
		} catch (IOException e) {
			logger.error("Encountered exception while trying to close reader", e);
			return null;
		}
		return object;
	}

	public void write(JsonElement object, File file){
		if (!file.exists()){
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("Encountered an exception while trying to create data to file {}", file.getName(), e);
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(gson.toJson(object));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("Encountered an exception while trying to save data to file {}", file.getName(), e);
		}
	}

	File getDataFolder(UUID uniqueId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/");
	}
	File getDataFile(UUID uniqueId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/data.json");
	}
	File getSettingsFile(UUID uniqueId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/settings.json");
	}
	File getBannedFile(UUID uniqueId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/banned.json");
	}
	File getUserFolder(UUID uniqueId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/users/");
	}
	File getUserFile(UUID uniqueId, UUID userId){
		return new File(unity.getDataFolder(), "factions/"+uniqueId+"/users/"+userId+".json");
	}

	File createIfNotExists(File file) throws IOException {;
		if (!file.exists()){
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		return file;
	}

	@Override
	public CompletableFuture<Boolean> exists(@NotNull UUID uniqueId) {
		return CompletableFuture.supplyAsync(()-> (getDataFolder(uniqueId) == null || !getDataFolder(uniqueId).exists()));
	}

	@Override
	public CompletableFuture<Boolean> exists(@NotNull String lookingFor) {
		return CompletableFuture.supplyAsync(()->{
			File file = new File(unity.getDataFolder(), "factions.json");
			if (!file.exists()){
				return false;
			}
			JsonArray array = read(file, JsonArray.class);
			for (JsonElement element : array){
				JsonObject object = element.getAsJsonObject();
				String name = object.get("name").getAsString();
				if (lookingFor.equalsIgnoreCase(name)){
					return true;
				}
			}
			return false;
		});
	}

	@Override
	public CompletableFuture<Void> save(@NotNull Map<String, UUID> cachedNames) {
		return CompletableFuture.runAsync(()->{
			JsonArray array = new JsonArray();
			for (Map.Entry<String, UUID> entry : cachedNames.entrySet()){
				JsonObject object = new JsonObject();
				object.addProperty("name", entry.getKey());
				object.addProperty("id", entry.getValue().toString());
				array.add(object);
			}
			try {
				write(array, createIfNotExists(new File(unity.getDataFolder(), "factions.json")));
			} catch (IOException e) {
				logger.error("Encountered exception while trying to save to file", e);
			}
		});
	}

	@Override
	public CompletableFuture<Collection<UUID>> getAllExistingIds() {
		return CompletableFuture.supplyAsync(()->{
			File file = new File(unity.getDataFolder(), "factions/");
			if (!file.exists()){
				return List.of();
			}
			Collection<UUID> uniqueIds = new LinkedList<>();
			if (file.isFile()) {
				return List.of();
			}
			File[] files = file.listFiles();
			if (files == null){
				return List.of();
			}
			for (File subFolder : files){
				uniqueIds.add(UUID.fromString(subFolder.getName().split("\\.")[0]));
			}
			return uniqueIds;
		});
	}

	@Override
	public CompletableFuture<Collection<String>> getAllExistingNames() {
		return CompletableFuture.supplyAsync(()->{
			File file = new File(unity.getDataFolder(), "factions.json");
			if (!file.exists()){
				return List.of();
			}
			Collection<String> names = new LinkedList<>();
			JsonArray array = read(file, JsonArray.class);
			for (JsonElement element : array){
				JsonObject object = element.getAsJsonObject();
				String name = object.get("name").getAsString();
				names.add(name);
			}
			return names;
		});
	}

	@Override
	public CompletableFuture<UUID> getIdFromName(@NotNull String lookingFor) {
		return CompletableFuture.supplyAsync(()->{
			File file = new File(unity.getDataFolder(), "factions.json");
			if (!file.exists()){
				return null;
			}
			JsonArray array = read(file, JsonArray.class);
			for (JsonElement element : array){
				JsonObject object = element.getAsJsonObject();
				String name = object.get("name").getAsString();
				if (lookingFor.equals(name)) {
					return UUID.fromString(object.get("id").getAsString());
				}
			}
			return null;
		});
	}

	@Override
	public CompletableFuture<String> getNameFromId(@NotNull UUID lookingFor) {
		return CompletableFuture.supplyAsync(()->{
			File file = new File(unity.getDataFolder(), "factions.json");
			if (!file.exists()){
				return null;
			}
			JsonArray array = read(file, JsonArray.class);
			for (JsonElement element : array){
				JsonObject object = element.getAsJsonObject();
				String name = object.get("name").getAsString();
				UUID uniqueId = UUID.fromString(object.get("id").getAsString());
				if (lookingFor.equals(uniqueId)) {
					return name;
				}
			}
			return null;
		});
	}
}
