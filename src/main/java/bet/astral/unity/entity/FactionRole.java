package bet.astral.unity.entity;

import bet.astral.messenger.v2.translation.Translation;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

@Getter
public class FactionRole implements Entity {
	private final UUID uniqueId;
	private final Translation name;
	private final Translation prefix;
	private final Translation prefixName;
	private final int priority;
	private final Set<Permission> permissions;
	public final static FactionRole OWNER = new FactionRoleBuilder(UUID.fromString("1aa5544c-516c-11ef-9454-0242ac120002"), Translations.ROLE_OWNER_NAME, Translations.ROLE_OWNER_PREFIX, Translations.ROLE_OWNER_PREFIX_PLAYER)
			.permissions(Permission.KICK_MEMBERS, Permission.BAN_PLAYERS)
			.priority(50)
			.build();
	public final static FactionRole ADMINISTRATOR = new FactionRoleBuilder(UUID.fromString("3644de5c-516c-11ef-9454-0242ac120002"), Translations.ROLE_ADMIN_NAME, Translations.ROLE_ADMIN_PREFIX, Translations.ROLE_ADMIN_PREFIX_PLAYER)
			.permissions(Permission.KICK_MEMBERS, Permission.BAN_PLAYERS)
			.priority(20)
			.build();
	public final static FactionRole MODERATOR = new FactionRoleBuilder(UUID.fromString("3644e0be-516c-11ef-9454-0242ac120002"), Translations.ROLE_MOD_NAME, Translations.ROLE_MOD_PREFIX, Translations.ROLE_MOD_PREFIX_PLAYER)
			.permissions(Permission.KICK_MEMBERS)
			.priority(10)
			.build();
	public final static FactionRole MEMBER = new FactionRoleBuilder(UUID.fromString("3644e1f4-516c-11ef-9454-0242ac120002"), Translations.ROLE_MEMBER_NAME, Translations.ROLE_MEMBER_PREFIX, Translations.ROLE_MEMBER_PREFIX_PLAYER)
			.priority(5)
			.build();
	public final static FactionRole GUEST = new FactionRoleBuilder(UUID.fromString("3644e302-516c-11ef-9454-0242ac120002"), Translations.ROLE_GUEST_NAME, Translations.ROLE_GUEST_PREFIX, Translations.ROLE_GUEST_PREFIX_PLAYER)
			.priority(0)
			.build();

	@Contract(pure = true)
	public FactionRole(UUID uniqueId, Translation name, Translation prefix, Translation prefixName, int priority, Set<Permission> permissions) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.prefix = prefix;
		this.prefixName = prefixName;
		this.priority = priority;
		this.permissions = permissions;
	}

	public boolean hasPermission(Permission permission){
		return permissions.contains(permission);
	}

	@NotNull
	public static FactionRole fromId(@NotNull UUID role) {
		if (role.equals(OWNER.uniqueId)) {
			return OWNER;
		} else if (role.equals(ADMINISTRATOR.uniqueId)) {
			return ADMINISTRATOR;
		} else if (role.equals(MODERATOR.uniqueId)) {
			return MODERATOR;
		} else if (role.equals(MEMBER.uniqueId)) {
			return MEMBER;
		}
		return GUEST;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	public boolean canKick(FactionRole role) {
		return role.getPriority()<priority;
	}
}
