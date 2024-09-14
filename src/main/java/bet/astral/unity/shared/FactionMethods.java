package bet.astral.unity.shared;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderMap;
import bet.astral.messenger.v2.placeholder.values.PlaceholderValue;
import bet.astral.unity.Unity;
import bet.astral.unity.data.FactionDatabase;
import bet.astral.unity.data.PlayerDatabase;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.entity.FactionRole;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import bet.astral.unity.permission.Permission;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

import static bet.astral.unity.messenger.UnityMessenger.placeholders;

@Getter
public class FactionMethods {
	private final Unity unity;
	private final FactionManager manager;
	private final FactionDatabase database;
	private final PlayerManager playerManager;
	private final PlayerDatabase playerDatabase;
	private final UnityMessenger messenger;

	public FactionMethods(Unity unity) {
		this.unity = unity;
		this.manager = unity.getFactionManager();
		this.database = unity.getFactionDatabase();
		this.playerManager = unity.getPlayerManager();
		this.playerDatabase = unity.getPlayerDatabase();
		this.messenger = unity.getMessenger();
	}

	private Unity unity(){
		return unity;
	}

	/**
	 * Makes player join a faction. Uses first player parameter to join the given faction.
	 * @param player player
	 * @param faction faction
	 * @param menuFunction menu open function
	 */
	public void join(@NotNull Player player, @NotNull Faction faction, @Nullable Consumer<Player> menuFunction) {
		PlaceholderMap placeholders = new PlaceholderMap();
		placeholders.add("player", player.name());
		placeholders.add("faction", faction.getName());
		if (faction.isBanned(player.getUniqueId())) {
			getMessenger().message(player, Translations.MESSAGE_JOIN_BANNED_FROM_FACTION, placeholders);
			if (menuFunction != null){
				menuFunction.accept(player);
			}
			return;
		}

		if (!faction.isPublic() && !faction.isInvited(player.getUniqueId())){
			getMessenger().message(player, Translations.MESSAGE_JOIN_NOT_INVITED, placeholders);
			if (menuFunction != null){
				menuFunction.accept(player);
			}
			return;
		}

		if (faction.isInvited(player.getUniqueId())){
			faction.cancelInvite(player.getUniqueId());
			getMessenger().message(player, Translations.MESSAGE_JOIN_JOINED_USING_INVITE, placeholders);
			faction.message(Translations.BROADCAST_JOIN_JOINED_USING_INVITE, placeholders);
		} else {
			getMessenger().message(player, Translations.MESSAGE_JOIN_JOINED_PUBLIC, placeholders);
			faction.message(Translations.BROADCAST_JOIN_JOINED_PUBLIC, placeholders);
		}
		FactionMember member = new FactionMember(messenger, player.getUniqueId(), System.currentTimeMillis());
		member.setRole(FactionRole.GUEST);
		faction.getMembers().put(player.getUniqueId(), member);
	}

	/**
	 * Kicks a member from the faction. Uses first player parameter to kick second parameter player from faction.
	 * @param player player who kicked
	 * @param kicking player who was kicked
	 * @param reason reason to kick
	 * @param menuFunction menu open function
	 */
	@Contract(pure = true)
	public void kickPlain(@NotNull Player player, @NotNull OfflinePlayer kicking, @Nullable String reason, @Nullable Consumer<Player> menuFunction){
		kick(player, kicking, reason != null ? Component.text(reason) : null, menuFunction);
	}

	/**
	 * Kicks a member from the faction. Uses first player parameter to kick second parameter player from faction.
	 * @param player player who kicked
	 * @param kicking player who was kicked
	 * @param reason reason to kick
	 * @param menuFunction menu open function
	 */
	public void kick(@NotNull Player player, @NotNull OfflinePlayer kicking, @Nullable Component reason, @Nullable Consumer<Player> menuFunction){
		bet.astral.unity.entity.Player fPlayer = unity().getPlayerManager().fromBukkit(player);
		if (fPlayer.getFactionId() == null) {
			if (menuFunction != null)
				unity().getGuiHandler().openMainMenu(player);
			return;
		}
		Faction faction = unity().getFactionManager().get(fPlayer.getFactionId());
		if (faction == null) {
			if (menuFunction != null)
				unity().getGuiHandler().openMainMenu(player);
			return;
		}
		UUID kickingId = kicking.getUniqueId();
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders(player, faction));
		placeholders.add("who", kicking.getName());
		if (reason==null){
			placeholders.add("reason", PlaceholderValue.translation(Translations.MESSAGE_FACTION_KICK_NO_REASON, ComponentType.CHAT));
		} else {
			placeholders.add("reason", Component.join(JoinConfiguration.spaces(), reason));
		}

		FactionMember member = faction.getMember(player.getUniqueId());
		FactionMember kickingMember = faction.getMember(kickingId);
		if (kickingMember == null) {
			if (menuFunction != null) {
				menuFunction.accept(player);
			}
			unity().message(player, Translations.MESSAGE_PLAYER_PLAYER_IS_NOT_A_CLAN_MEMBER, placeholders);
			return;
		}
		if (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.BAN_PLAYERS)) {
			if (!member.getRole().canKick(kickingMember.getRole())) {
				unity().message(player, Translations.MESSAGE_FACTION_KICK_CANNOT_KICK_USER, placeholders);
				if (menuFunction != null) {
					menuFunction.accept(player);
				}
				return;
			}

			faction.getMembers().remove(kickingId);
			unity().getFactionDatabase().save(faction);
			unity().getPlayerManager().getOrLoadAndCache(kickingId)
					.thenAccept(p -> {
						p.setFactionId(null);
						unity().getPlayerDatabase().save(p);
						if (!kicking.isOnline()) {
							unity().getPlayerManager().unload(kickingId);
						}
					});

			unity().message(player, Translations.MESSAGE_FACTION_KICK_KICKED, placeholders);
			faction.message(Translations.BROADCAST_FACTION_KICK_KICKED, placeholders);
			unity().message(kicking, Translations.MESSAGE_FACTION_KICK_WHO_WAS_KICKED, placeholders);
		} else {
			unity().message(player, Translations.MESSAGE_FACTION_KICK_NO_PERMISSIONS, placeholders);
			if (menuFunction != null) {
				menuFunction.accept(player);
			}
		}
	}

	/**
	 * Bans given player from the faction using first argument of the method as the player who banned them.
	 * @param player player who banned
	 * @param banning player who was banned
	 * @param reason reason for the ban
	 * @param menuFunction function to open a GUI menu
	 */
	public void ban(@NotNull Player player, @NotNull OfflinePlayer banning, @Nullable Component reason, @Nullable Consumer<Player> menuFunction){
		bet.astral.unity.entity.Player fPlayer = unity().getPlayerManager().fromBukkit(player);
		if (fPlayer.getFactionId() == null) {
			if (menuFunction != null)
				unity().getGuiHandler().openMainMenu(player);
			return;
		}
		Faction faction = unity().getFactionManager().get(fPlayer.getFactionId());
		if (faction == null) {
			if (menuFunction != null)
				unity().getGuiHandler().openMainMenu(player);
			return;
		}
		UUID id = banning.getUniqueId();


		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders(player, faction));
		placeholders.add("who", banning.getName());

		if (reason == null){
			placeholders.add("reason", PlaceholderValue.translation(Translations.MESSAGE_FACTION_BAN_NO_REASON, ComponentType.CHAT));
		} else {
			placeholders.add("reason", reason);
		}

		FactionMember member = faction.getMember(player.getUniqueId());
		FactionMember kickingMember = faction.getMember(id);
		if (member.hasPermission(Permission.BAN_PLAYERS)) {
			if (faction.isBanned(id)){
				placeholders.add("old_reason", faction.getBan(id).getReason());
				unity().message(player, Translations.MESSAGE_FACTION_BAN_ALREADY_BANNED, placeholders);
				if (menuFunction != null)
					menuFunction.accept(player);
				return;
			}
			if (kickingMember != null) {
				if (!member.getRole().canKick(kickingMember.getRole())) {
					unity().message(player, Translations.MESSAGE_FACTION_BAN_CANNOT_BAN_USER, placeholders);
					if (menuFunction != null)
						menuFunction.accept(player);
					return;
				}
			}

			faction.ban(id, reason);
			unity().getFactionDatabase().save(faction);
			unity().getPlayerManager().getOrLoadAndCache(id)
					.thenAccept(p -> {
						p.setFactionId(null);
						unity().getPlayerDatabase().save(p);
						if (!banning.isOnline()) {
							unity().getPlayerManager().unload(id);
						}
					});

			unity().message(player, Translations.MESSAGE_FACTION_BAN_BANNED, placeholders);
			unity().message(faction, Translations.BROADCAST_FACTION_BAN_BANNED, placeholders);
			unity().message(id, Translations.MESSAGE_FACTION_BAN_WHO_WAS_BANNED, placeholders);
		} else {
			unity().message(player, Translations.MESSAGE_FACTION_BAN_NO_PERMISSIONS, placeholders);
			if (menuFunction != null)
				menuFunction.accept(player);
		}
	}

	/**
	 * Bans given player from the faction using first argument of the method as the player who banned them.
	 * Uses {@link Component#text(String)} to convert given reason to component.
	 * @param player player who banned
	 * @param banning player who was banned
	 * @param reason reason for the ban
	 * @param menuFunction function to open a GUI menu
	 */
	@Contract(pure = true)
	public void banPlain(@NotNull Player player, @NotNull OfflinePlayer banning, @Nullable String reason, @Nullable Consumer<Player> menuFunction){
		ban(player, banning, reason != null ? Component.text(reason) : null, menuFunction);
	}


	/**
	 * Invites given player to the faction, and handles all messages and
	 * @param player player who sent invite
	 * @param whoToInvite player who was invited
	 * @param menuFunction menu open function run when returning
	 */
	public void invite(@NotNull Player player, @Nullable Player whoToInvite, @Nullable Consumer<Player> menuFunction){
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());

		if (faction == null){
			if (menuFunction != null) {
				unity
						.getGuiHandler()
						.getFactionGUI()
						.openMainMenu(player);
			}
			return;
		}

		if (whoToInvite == null){
			if (menuFunction != null) {
				menuFunction.accept(player);
			}
			return;
		}
		PlaceholderList placeholders = UnityMessenger.placeholders(player, faction);
		placeholders.add("who", whoToInvite.getName());
		faction.invite(whoToInvite.getUniqueId());
		getMessenger().message(whoToInvite, Translations.MESSAGE_FACTION_INVITE_RECEIVE, placeholders);
		getMessenger().message(player, Translations.MESSAGE_FACTION_INVITE_SENT, placeholders);
		getMessenger().message(faction, Translations.BROADCAST_FACTION_INVITE_SENT, placeholders);
		if (menuFunction != null) {
			menuFunction.accept(player);
		}
	}

	/**
	 * Cancels the invite sent and opens cancel invite gui if possible
	 * @param player player revokes
	 * @param whoToRevoke who was invited
	 * @param menuFunction menu open function run when returning
	 */
	public void cancelInvite(Player player, @Nullable OfflinePlayer whoToRevoke, Consumer<Player> menuFunction) {
		bet.astral.unity.entity.Player fPlayer = playerManager.fromBukkit(player);
		Faction faction = manager.get(fPlayer.getFactionId());
		if (faction == null){
			if (menuFunction != null) {
				unity
						.getGuiHandler()
						.openMainMenu(player);
			}
			return;
		}

		if (whoToRevoke == null){
			if (menuFunction != null) {
				menuFunction.accept(player);
			}
			return;
		}
		PlaceholderList placeholders = UnityMessenger.placeholders(player, faction);
		placeholders.add("who", whoToRevoke.getName());
		faction.cancelInvite(whoToRevoke.getUniqueId());
		getMessenger().message(whoToRevoke, Translations.MESSAGE_FACTION_INVITE_RECEIVE, placeholders);
		getMessenger().message(player, Translations.MESSAGE_FACTION_INVITE_SENT, placeholders);
		getMessenger().message(faction, Translations.BROADCAST_FACTION_INVITE_SENT, placeholders);
		if (menuFunction != null) {
			menuFunction.accept(player);
		}

	}

	/**
	 * Makes the player leave their current faction.
	 * @param player player
	 * @param menuFunction function ran when executed
	 */
	public void leave(@NonNull Player player, Consumer<Player> menuFunction) {
		bet.astral.unity.entity.Player fPlayer = playerManager.fromBukkit(player);
		if (fPlayer.getFactionId()==null){
			if (menuFunction != null){
				unity.getGuiHandler().openMainMenu(player);
			}
			return;
		}

		Faction faction = manager.get(fPlayer.getFactionId());
		assert faction != null;

		FactionMember member = faction.getMember(player.getUniqueId());
		if (member.getRole()==FactionRole.OWNER){
			if (menuFunction != null){
				unity.getGuiHandler().openMainMenu(player);
			}
			return;
		}

		fPlayer.setFactionId(null);
		faction.getMembers().remove(member.getUniqueId());
		database.save(faction);
		playerDatabase.save(fPlayer);

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.getName());
		placeholders.add("faction", faction.getName());
		placeholders.add("role_name", PlaceholderValue.translation(member.getRole().getPrefixName(), ComponentType.CHAT));
		placeholders.add("role_player", PlaceholderValue.translation(member.getRole().getPrefix(), ComponentType.CHAT, Placeholder.of("player", player.name())));
		getMessenger().message(player, Translations.MESSAGE_FACTION_LEAVE, placeholders);
		faction.message(Translations.BROADCAST_FACTION_LEAVE, placeholders);

		if (menuFunction != null){
			menuFunction.accept(player);
		}
	}

	public void delete(Player player) {
		bet.astral.unity.entity.Player fPlayer = unity().getPlayerManager().fromBukkit(player);
		if (fPlayer.getFactionId()==null){
			// Unlikely
			return;
		}
		Faction faction = unity().getFactionManager().get(fPlayer.getFactionId());
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.getName());
		placeholders.add("faction", faction.getName());

		getMessenger().message(player, Translations.MESSAGE_FACTION_DELETE_CONFIRMED, placeholders);
		faction.message(Translations.BROADCAST_FACTION_DELETE_CONFIRMED, placeholders);
		unity().getFactionManager().unload(faction.getUniqueId());
		unity().getFactionDatabase().delete(faction.getUniqueId());
		unity().getFactionInfoDatabase().delete(faction.getUniqueId());

		for (FactionMember member : faction.getMembers().values()) {
			boolean cached = unity().getPlayerManager().get(member.getUniqueId()) != null;
			unity().getPlayerManager().getOrLoadAndCache(member.getUniqueId())
					.thenAccept(memberPlayer->{
						memberPlayer.setFactionId(null);
						unity().getPlayerDatabase().save(memberPlayer);
						if (!cached){
							unity().getPlayerManager().unload(memberPlayer.getUniqueId());
						}
					});
		}
	}
}
