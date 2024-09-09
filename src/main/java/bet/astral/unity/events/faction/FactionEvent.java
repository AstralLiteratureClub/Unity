package bet.astral.unity.events.faction;

import bet.astral.more4j.event.Event;
import bet.astral.unity.entity.Faction;
import lombok.Getter;

@Getter
public class FactionEvent implements Event {
	private final Faction faction;

	public FactionEvent(Faction faction) {
		this.faction = faction;
	}
}
