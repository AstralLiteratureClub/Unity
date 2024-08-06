package bet.astral.unity.entity;

import bet.astral.messenger.v2.DefaultScheduler;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.receiver.Forwarder;
import bet.astral.messenger.v2.task.IScheduler;
import bet.astral.unity.messenger.UnityMessenger;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class FactionMember implements Entity, Forwarder {
	private final UUID uniqueId;
	private FactionRole role;
	private final UnityMessenger messenger;
	private final long firstJoined;

	public FactionMember(UnityMessenger messenger, UUID uniqueId) {
		this(messenger, uniqueId, System.currentTimeMillis());
	}
	public FactionMember(UnityMessenger messenger, UUID uniqueId, long firstJoined) {
		this.uniqueId = uniqueId;
		this.messenger = messenger;
		this.firstJoined = firstJoined;
		role = FactionRole.GUEST;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public @NotNull IScheduler getScheduler() {
		if (Bukkit.getPlayer(uniqueId) == null){
			return DefaultScheduler.ASYNC_SCHEDULER;
		}
		return messenger.asReceiver(Bukkit.getPlayer(uniqueId)).getScheduler();
	}

	@Override
	public @NotNull Locale getLocale() {
		return Locale.US;
	}

	@Override
	public boolean hasPermission(@NotNull Permission permission) {
		return permission.test(this);
	}

	@Override
	public boolean hasPermission(@NotNull String s) {
		if (Bukkit.getPlayer(uniqueId) == null){
			return false;
		}
		return Bukkit.getPlayer(uniqueId).hasPermission(s);
	}

	public boolean hasPermission(@NotNull bet.astral.unity.permission.Permission permission){
		return role.hasPermission(permission);
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		if (Bukkit.getPlayer(uniqueId)==null){
			return List.of();
		}
		return List.of(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
	}

	@Override
	public @NotNull Messenger getMessenger() {
		return messenger;
	}
}
