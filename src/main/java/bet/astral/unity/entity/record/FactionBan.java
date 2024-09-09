package bet.astral.unity.entity.record;

import bet.astral.unity.entity.Entity;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class FactionBan implements Entity {
	@NotNull
	private final UUID uniqueId;
	@NotNull
	private final String reason;

	public FactionBan(@NotNull UUID uniqueId, @NotNull Component reason) {
		this.uniqueId = uniqueId;
		this.reason = GsonComponentSerializer.gson().serialize(reason);
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	public @NotNull Component getReason() {
		return GsonComponentSerializer.gson().deserialize(reason);
	}

	public @NotNull Component getReasonJson(){
		return GsonComponentSerializer.gson().deserialize(reason);
	}
}
