package bet.astral.unity.entity.record;

import bet.astral.unity.entity.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FactionInvite implements Entity {
	private final UUID uniqueId;
	private final UUID factionId;
	private final long expires;

	public FactionInvite(UUID uniqueId, UUID factionId, long expires) {
		this.uniqueId = uniqueId;
		this.factionId = factionId;
		this.expires = expires;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}
}
