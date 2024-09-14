package bet.astral.unity.commands;

import bet.astral.messenger.v2.Messenger;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.entity.FactionRole;
import org.bukkit.entity.Player;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.UUID;

public interface BaseCommand {
	Unity getUnity();
	Messenger getMessenger();

	default Permission requireNoFaction(String name) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			try {
				return getUnity().getPlayerManager().fromBukkit(player).getFactionId() == null;
			} catch (NullPointerException e){
				return true;
			}
		}));
	}
	default Permission requireFaction(String name) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = getUnity().getPlayerManager().fromBukkit(player).getFactionId();
			return uniqueId != null;
		}));
	}

	default Permission requireHaveRole(String name, FactionRole... roles) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = getUnity().getPlayerManager().fromBukkit(player).getFactionId();
			if (uniqueId == null){
				return false;
			}
			UUID factionId = getUnity().getPlayerManager().fromBukkit(player).getFactionId();
			Faction faction = getUnity().getFactionManager().get(factionId);
			FactionMember member = faction.getMember(player.getUniqueId());

			for (FactionRole role : roles){
				if (member.getRole()==role){
					return true;
				}
			}

			return false;
		}));
	}
	default Permission requireNotHaveRole(String name, FactionRole... roles) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = getUnity().getPlayerManager().fromBukkit(player).getFactionId();
			if (uniqueId == null){
				return false;
			}
			UUID factionId = getUnity().getPlayerManager().fromBukkit(player).getFactionId();
			Faction faction = getUnity().getFactionManager().get(factionId);
			FactionMember member = faction.getMember(player.getUniqueId());

			for (FactionRole role : roles){
				if (member.getRole()==role){
					return false;
				}
			}

			return true;
		}));
	}
}
