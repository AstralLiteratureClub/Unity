package bet.astral.unity.entity.record;

import bet.astral.unity.entity.Entity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class FactionBan implements Entity {
	@NotNull
	private final UUID uniqueId;
	@NotNull
	private final String reason;

	public FactionBan(@NotNull UUID uniqueId, @NotNull String reason) {
		this.uniqueId = uniqueId;
		this.reason = reason;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}
}
