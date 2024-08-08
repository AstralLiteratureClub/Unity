package bet.astral.unity.events.faction;

import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.record.FactionInvite;
import lombok.Getter;

@Getter
public class FactionInviteExpireEvent extends FactionEvent{
	private final FactionInvite invite;
	public FactionInviteExpireEvent(Faction faction, FactionInvite invite) {
		super(faction);
		this.invite = invite;
	}
}
