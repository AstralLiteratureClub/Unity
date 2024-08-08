package bet.astral.unity.events.faction;

import bet.astral.unity.entity.Faction;
import bet.astral.unity.events.Event;
import lombok.Getter;

@Getter
public class FactionEvent implements Event {
	private final Faction faction;

	public FactionEvent(Faction faction) {
		this.faction = faction;
	}
}
