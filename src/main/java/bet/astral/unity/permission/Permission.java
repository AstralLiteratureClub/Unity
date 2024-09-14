package bet.astral.unity.permission;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;

public class Permission implements PredicatePermission<CommandSender> {
	public static final Permission BAN_PLAYERS = new Permission("unity:ban");
	public static final Permission KICK_MEMBERS = new Permission("unity:kick");
//	public static final Permission BEGIN_WAR = new Permission("unity:ban"),
//	public static final Permission BEGIN_WAR_VOTE,
	public static final Permission INVITE = new Permission("unity:invite");
	public static final Permission CANCEL_INVITE = new Permission("unity:cancel_invite");

	private static Unity unity;
	private final String name;

	public Permission(String name) {
		this.name = name;
	}

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

