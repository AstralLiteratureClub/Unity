package bet.astral.unity.data.gson;

import bet.astral.unity.Unity;
import bet.astral.unity.data.PlayerDatabase;
import bet.astral.unity.entity.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GsonPlayerDatabase implements PlayerDatabase {
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	private final Unity unity;
	private final Logger logger;

	public GsonPlayerDatabase(Unity unity) {
		this.unity = unity;
		logger = unity.getComponentLogger();
	}

	@Override
	public CompletableFuture<@Nullable Player> load(@NotNull UUID player) {
		return update(new Player(player));
	}

	@Override
	public CompletableFuture<Void> save(@NotNull Player player) {
		return CompletableFuture.runAsync(()->{
			JsonObject object = new JsonObject();
			object.addProperty("uniqueId", player.getUniqueId().toString());
			if (player.getFactionId()!=null){
				object.addProperty("factionId", player.getFactionId().toString());
			}
			try {
				File file = createIfNotExists(getDataFile(player.getUniqueId()));
				write(object, file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public CompletableFuture<@NotNull Player> update(@NotNull Player player) {
		return CompletableFuture.supplyAsync(()->{
			if (!getDataFile(player.getUniqueId()).exists()){
				return player;
			}
			JsonObject object = read(getDataFile(player.getUniqueId()));
			UUID factionId = null;
			if (object.get("factionId") != null){
				factionId = UUID.fromString(object.get("factionId").getAsString());
			}
			player.setFactionId(factionId);

			return player;
		});
	}

	public JsonObject read(File file){
		return read(file, JsonObject.class);
	}

	public <T> T read(File file, Class<T> clazz){
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

	public void write(JsonObject object, File file){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(gson.toJson(object));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("Encountered an exception while trying to save data to file {}", file.getName(), e);
		}
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

	public File getDataFolder(){
		return new File(unity.getDataFolder(), "users/");
	}
	public File getDataFile(UUID uniqueId){
		return new File(unity.getDataFolder(), "users/"+uniqueId+".json");
	}
}
