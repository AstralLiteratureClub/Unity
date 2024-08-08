package bet.astral.unity.entity;

import bet.astral.unity.Unity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Player implements Entity {
	private static Unity unity = Unity.getPlugin(Unity.class);
	private final UUID uniqueId;
	private UUID factionId;

	public Player(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public UUID getFactionIdIgnoreChecks(){
		return factionId;
	}
	public UUID getFactionId() {
		if (factionId != null) {
			Faction faction = unity.getFactionManager().get(factionId);
			if (faction == null) {
				return null;
			}
			if (faction.getMember(uniqueId) == null){
				return null;
			}
			return factionId;
		}
		return null;
	}
}
