package bet.astral.unity.gui;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.utils.MessageSender;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.entity.FactionRole;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import de.cubbossa.translations.ComponentSplit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseGUI implements MessageSender.Packed {
	private static final Random random = new Random(System.currentTimeMillis()*432);
	protected final Unity unity;
	protected final GUIHandler handler;
	protected static final MiniMessage miniMessage = MiniMessage.miniMessage();
	protected static final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
	@Contract(pure = true)
	public BaseGUI(@NotNull BaseGUI baseGUI){
		this.unity = baseGUI.unity;
		this.handler = baseGUI.handler;
	}
	@Contract(pure = true)
	public BaseGUI(@NotNull GUIHandler guiHandler){
		this.unity = guiHandler.getUnity();
		this.handler = guiHandler;
	}

	@Override
	public Messenger getMessenger() {
		return unity.getMessenger();
	}


	public List<? extends Component> splitComponent(Player player, TranslationKey translation, Placeholder... placeholders) {
		Component component = component(player, translation, placeholders);
		if (component==null){
			return List.of();
		}
		return ComponentSplit.split(component, "\n");
	}

	@Deprecated(forRemoval = true)
	public Component component(@NotNull Player player, TranslationKey translation, Placeholder... placeholders) {
		return unity.getMessenger().disablePrefixForNextParse().parseComponent(translation, player.locale(), ComponentType.CHAT, placeholders);
	}
	public List<? extends Component> splitComponent(Player player, TranslationKey translation, @NotNull Collection<Placeholder> placeholders) {
		return splitComponent(player, translation, placeholders.toArray(Placeholder[]::new));
	}

	@Deprecated(forRemoval = true)
	public Component component(Player player, TranslationKey translation, @NotNull Collection<Placeholder> placeholders) {
		return component(player, translation, placeholders.toArray(Placeholder[]::new));
	}

	protected PlaceholderList placeholders(@NotNull OfflinePlayer player, @Nullable Faction faction) {
		return UnityMessenger.placeholders(player, faction);
	}

	public void overrideToolTooltips(@NotNull ItemMeta meta){
		meta.addItemFlags(ItemFlag.values());
	}

	public Optional<Faction> fetchFaction(OfflinePlayer player) {
		if (unity.getPlayerManager().get(player.getUniqueId())==null){
			return Optional.empty();
		}
		if (Objects.requireNonNull(unity.getPlayerManager()
				.get(player.getUniqueId())).getFactionId() == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(
				unity.getFactionManager().get(
						Objects.requireNonNull(unity.getPlayerManager()
								.get(player.getUniqueId())).getFactionId()));
	}

	@SuppressWarnings("SameParameterValue")
	protected ClickableBuilder randomMember(@NotNull Player player, @NotNull Faction faction, @NotNull Translation name, @NotNull Translation description) {
		return Clickable.builder(
				Material.PLAYER_HEAD,
				meta->{
					FactionMember randomMember = faction.getRandomMember();
					OfflinePlayer randomOfflinePlayer = Bukkit.getOfflinePlayer(randomMember.getUniqueId());
					meta.setPlayerProfile(randomOfflinePlayer.getPlayerProfile());
				},
				SkullMeta.class
		).title(name).description(description).placeholderGenerator(p->placeholders(player, faction));
	}
	protected ClickableBuilder member(@NotNull Player player, @NotNull UUID uniqueId, @NotNull Faction faction, @NotNull Translation name, @NotNull Translation description) {
		return Clickable.builder(
				Material.PLAYER_HEAD,
				meta->{
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
					meta.setPlayerProfile(offlinePlayer.getPlayerProfile());

					PlaceholderList placeholders = placeholders(player, faction);

					meta.displayName(component(player, name, placeholders));
					meta.lore(splitComponent(player, description, placeholders));
				},
				SkullMeta.class
		);
	}
	@SuppressWarnings("SameParameterValue")
	protected ClickableBuilder randomPlayer(@NotNull Player player, @NotNull Collection<? extends OfflinePlayer> players, @NotNull Translation name, @NotNull Translation description) {
		return Clickable.builder(
				Material.PLAYER_HEAD,
				meta -> {
					List<? extends OfflinePlayer> playersList = new LinkedList<>(players);
					OfflinePlayer offlinePlayer;
					if (playersList.isEmpty()) {
						return;
					} else if (playersList.size() == 1) {
						offlinePlayer = playersList.getFirst();
					} else {
						offlinePlayer = playersList.get(random.nextInt(0, random.nextInt(playersList.size())));
					}
					meta.setPlayerProfile(offlinePlayer.getPlayerProfile());

				},
				SkullMeta.class
		)
				.placeholderGenerator(p -> placeholders(player, null))
				.title(name)
				.description(description)
				;
	}

	protected @NotNull ClickableBuilder createAccountInfo(Player player){
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		return Clickable.builder(
				Material.PLAYER_HEAD,
				meta->{
					meta.setPlayerProfile(player.getPlayerProfile());
				},
				SkullMeta.class)
				.placeholderGenerator(p->{
					Faction faction = null;
					if (fPlayer.getFactionId() != null){
						faction = unity.getFactionManager().get(fPlayer.getFactionId());
					}
					PlaceholderList placeholders = placeholders(player, faction);
					if (faction != null) {
						placeholders.add("faction", faction.getName());
						placeholders.add("faction_id", faction.getUniqueId().toString());
						placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
						FactionMember member = faction.getMember(player.getUniqueId());
						placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));

					}

					return placeholders;
				})
				.title(Translations.GUI_BUTTON_USER_INFO_NAME)
				.description(fPlayer.getFactionId() != null ? Translations.GUI_BUTTON_USER_INFO_FACTION_DESCRIPTION : Translations.GUI_BUTTON_USER_INFO_NO_FACTION_DESCRIPTION)
				;
	}

	public Permission requireNoFaction(String name) {
		return Permission.of(name).and(Permission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			try {
				return unity.getPlayerManager().fromBukkit(player).getFactionId() == null;
			} catch (NullPointerException e){
				return true;
			}
		}));
	}
	public Permission requireFaction(String name) {
		return Permission.of(name).and(Permission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = unity.getPlayerManager().fromBukkit(player).getFactionId();
			return uniqueId != null;
		}));
	}

	public Permission requireHaveRole(String name, FactionRole... roles) {
		return Permission.of(name).and(Permission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = unity.getPlayerManager().fromBukkit(player).getFactionId();
			if (uniqueId == null){
				return false;
			}
			UUID factionId = unity.getPlayerManager().fromBukkit(player).getFactionId();
			Faction faction = unity.getFactionManager().get(factionId);
			FactionMember member = faction.getMember(player.getUniqueId());

			for (FactionRole role : roles){
				if (member.getRole()==role){
					return true;
				}
			}

			return false;
		}));
	}
	public Permission requireNotHaveRole(String name, FactionRole... roles) {
		return Permission.of(name).and(Permission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = unity.getPlayerManager().fromBukkit(player).getFactionId();
			if (uniqueId == null){
				return false;
			}
			UUID factionId = unity.getPlayerManager().fromBukkit(player).getFactionId();
			Faction faction = unity.getFactionManager().get(factionId);
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
