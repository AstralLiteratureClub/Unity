package bet.astral.unity.entity;

import bet.astral.messenger.v2.translation.Translation;
import bet.astral.unity.permission.Permission;
import org.jetbrains.annotations.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FactionRoleBuilder {
	private final UUID uniqueId;
	private final Translation name;
	private final Translation prefix;
	private final Translation prefixName;
	private final Set<Permission> permissions = new HashSet<>();
	private int priority = 0;

	public FactionRoleBuilder(UUID uniqueId, Translation name, Translation prefix, Translation prefixName) {
		this.prefixName = prefixName;
		this.prefix = prefix;
		this.name = name;
		this.uniqueId = uniqueId;
	}

	public FactionRoleBuilder permission(Permission permission){
		this.permissions.add(permission);
		return this;
	}
	public FactionRoleBuilder permissions(Permission... permissions){
		this.permissions.addAll(List.of(permissions));
		return this;
	}

	public FactionRoleBuilder priority(@Range(from = 0, to = Integer.MAX_VALUE) int priority) {
		this.priority = priority;
		return this;
	}

	public FactionRole build() {
		return new FactionRole(uniqueId, name, prefix, prefixName, priority, permissions);
	}
}