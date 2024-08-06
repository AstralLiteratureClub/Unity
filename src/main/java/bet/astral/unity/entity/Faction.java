package bet.astral.unity.entity;

import bet.astral.messenger.v2.DefaultScheduler;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.task.IScheduler;
import bet.astral.unity.Unity;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
@Setter
public class Faction implements Entity, ForwardingAudience, Receiver {
	private static Unity unity;
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private final UUID uniqueId;
	private final Map<UUID, FactionMember> members = new HashMap<>();
	private final Set<FactionBan> banned = new HashSet<FactionBan>();
	private Locale locale = Locale.US;
	private String name;
	private final long firstCreated;

	public Faction(UUID uniqueId) {
		this(uniqueId, System.currentTimeMillis());
	}

	public Faction(UUID uniqueId, long firstCreated) {
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
	}

	private void fetchUnity(){
		unity = Unity.getPlugin(Unity.class);
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	@Blocking
	public List<? extends OfflinePlayer> getMembersAsOfflinePlayers(){
		return members.values().stream().map(FactionMember::getUniqueId).map(Bukkit::getOfflinePlayer).collect(Collectors.toList());
	}
	@Blocking
	public CompletableFuture<List<? extends OfflinePlayer>> getMembersAsOfflinePlayersASync(){
		return CompletableFuture.supplyAsync(()->members.values().stream().map(FactionMember::getUniqueId).map(Bukkit::getOfflinePlayer).collect(Collectors.toList()));
	}

	public List<? extends Player> getMembersAsPlayers(){
		return members.values().stream().map(FactionMember::getUniqueId).map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public boolean isMember(UUID uniqueId) {
		return members.get(uniqueId) != null;
	}

	public FactionMember getMember(@NotNull UUID uniqueId) {
		return members.get(uniqueId);
	}

	public FactionMember getRandomMember(){
		if (members.size()==1){
			return members.values().stream().toList().getFirst();
		}

		return members.values().stream().toList().get(RANDOM.nextInt(0, members.size()));
	}

	public void ban(@NotNull UUID uniqueId, String reason){
		fetchUnity();
		if (getMember(uniqueId) != null){
			members.remove(uniqueId);
			unity.getPlayerManager().getOrLoadAndCache(uniqueId)
					.thenAccept(p->{
						p.setFactionId(null);
						unity.getPlayerDatabase().save(p);
					});
		}
		banned.add(new FactionBan(uniqueId, reason));
		unity.getFactionDatabase().save(this);
	}

	public boolean isBanned(@NotNull UUID uniqueId){
		return banned.stream().anyMatch(ban->ban.getUniqueId().equals(uniqueId));
	}


	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return getMembersAsPlayers().stream().map(audience -> ((Audience) audience)).collect(Collectors.toSet());
	}

	@Override
	public @NotNull IScheduler getScheduler() {
		return DefaultScheduler.ASYNC_SCHEDULER;
	}

	@Override
	public @NotNull Locale getLocale() {
		return locale;
	}

	@Override
	public boolean hasPermission(@NotNull Permission permission) {
		return permission.test(this);
	}

	@Override
	public boolean hasPermission(@NotNull String s) {
		return false;
	}

	@Nullable
	public FactionBan getBan(UUID kickingId) {
		return banned.stream().filter(ban->ban.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
	}
}
