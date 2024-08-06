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

	public UUID getFactionId() {
		if (factionId == null){
			System.out.println("Null id");
		} else {
			Faction faction = unity.getFactionManager().get(factionId);
			if (faction == null) {
				System.out.println("Null");
				return null;
			}
			if (faction.getMember(uniqueId) == null){
				System.out.println("Null member");
				return null;
			}
			System.out.println("True");
			return factionId;
		}
		return null;
	}
}
