package bet.astral.unity.permission;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;

public enum Permission implements PredicatePermission<CommandSender> {
	KICK_MEMBERS,
	BAN_PLAYERS,
	BEGIN_WAR,
	BEGIN_WAR_VOTE,
	INVITE,
	CANCEL_INVITE
	;

	private static Unity unity;

	private Unity fetchUnity(){
		if (unity != null){
			unity = Unity.getPlugin(Unity.class);
		}
		return unity;
	}

	@Override
	public @NonNull PermissionResult testPermission(@NonNull CommandSender sender) {
		if (!(sender instanceof Player player)){
			return PermissionResult.denied(this);
		}
		if (unity == null){
			unity = Unity.getPlugin(Unity.class);;
		}
		if (unity.getPlayerManager().fromBukkit(player).getFactionId() == null){
			return PermissionResult.denied(this);
		}
		Faction faction = unity.getFactionManager().get(
				unity.getPlayerManager().fromBukkit(player).getFactionId());
		FactionMember member = faction.getMember(player.getUniqueId());
		return PermissionResult.of(member.hasPermission(this), this);
	}
}

