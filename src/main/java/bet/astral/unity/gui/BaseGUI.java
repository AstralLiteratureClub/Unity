package bet.astral.unity.gui;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.StaticBackground;
import bet.astral.guiman.background.builders.BorderPatternBuilder;
import bet.astral.guiman.background.builders.PatternBackgroundBuilder;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.messenger.v2.MessageSender;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseGUI implements MessageSender.Packed {
	private static final Random random = new Random(System.currentTimeMillis()*432);
	protected final Unity unity;
	protected final GUIHandler handler;
	protected static final MiniMessage miniMessage = MiniMessage.miniMessage();
	protected static final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
	public BaseGUI(@NotNull BaseGUI baseGUI){
		this.unity = baseGUI.unity;
		this.handler = baseGUI.handler;
	}
	public BaseGUI(GUIHandler guiHandler){
		this.unity = guiHandler.getUnity();
		this.handler = guiHandler;
	}

	@Override
	public Messenger getMessenger() {
		return unity.getMessenger();
	}


	public List<? extends Component> lore(Player player, TranslationKey translation, Placeholder... placeholders) {
		Component component = component(player, translation, placeholders);
		if (component==null){
			return List.of();
		}
		return ComponentSplit.split(component, "\n");
	}

	public Component component(@NotNull Player player, TranslationKey translation, Placeholder... placeholders) {
		return unity.getMessenger().disablePrefixForNextParse().parseComponent(translation, player.locale(), ComponentType.CHAT, placeholders);
	}
	public List<? extends Component> lore(Player player, TranslationKey translation, @NotNull Collection<Placeholder> placeholders) {
		return lore(player, translation, placeholders.toArray(Placeholder[]::new));
	}

	public Component component(Player player, TranslationKey translation, @NotNull Collection<Placeholder> placeholders) {
		return component(player, translation, placeholders.toArray(Placeholder[]::new));
	}

	protected PlaceholderList placeholders(@NotNull OfflinePlayer player, @Nullable Faction faction) {
		return UnityMessenger.placeholders(player, faction);
	}

	public void overrideToolTooltips(@NotNull ItemMeta meta){
		meta.addItemFlags(ItemFlag.values());
	}

	public Optional<Faction> fetchFaction(Player player) {
		if (unity.getPlayerManager()
				.fromBukkit(player).getFactionId() == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(
				unity.getFactionManager().get(
						unity.getPlayerManager()
								.fromBukkit(player).getFactionId()));
	}

	protected ClickableBuilder randomMember(Player player, Faction faction, Translation name, Translation description) {
		return new ClickableBuilder(
				Material.PLAYER_HEAD,
				meta->{
					FactionMember randomMember = faction.getRandomMember();
					OfflinePlayer randomOfflinePlayer = Bukkit.getOfflinePlayer(randomMember.getUniqueId());
					meta.setPlayerProfile(randomOfflinePlayer.getPlayerProfile());

					PlaceholderList placeholders = placeholders(player, faction);

					meta.displayName(component(player, name, placeholders));
					meta.lore(lore(player, description, placeholders));
				},
				SkullMeta.class
		);
	}
	protected ClickableBuilder member(Player player, UUID uniqueId, Faction faction, Translation name, Translation description) {
		return new ClickableBuilder(
				Material.PLAYER_HEAD,
				meta->{
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
					meta.setPlayerProfile(offlinePlayer.getPlayerProfile());

					PlaceholderList placeholders = placeholders(player, faction);

					meta.displayName(component(player, name, placeholders));
					meta.lore(lore(player, description, placeholders));
				},
				SkullMeta.class
		);
	}
	protected ClickableBuilder randomPlayer(Player player, Collection<? extends OfflinePlayer> players, Translation name, Translation description) {
		return new ClickableBuilder(
				Material.PLAYER_HEAD,
				meta->{
					meta.displayName(component(player, name, Placeholder.of("player", player.getName())));
					meta.lore(lore(player, description, Placeholder.of("player", player.getName())));

					List<? extends OfflinePlayer> playersList = new LinkedList<>(players);
					OfflinePlayer offlinePlayer;
					if (playersList.isEmpty()){
						return;
					} else if (playersList.size()==1){
						offlinePlayer = playersList.getFirst();
					} else {
						offlinePlayer = playersList.get(random.nextInt(0, random.nextInt(playersList.size())));
					}
					meta.setPlayerProfile(offlinePlayer.getPlayerProfile());

				},
				SkullMeta.class
		);
	}

	protected @NotNull ClickableBuilder createAccountInfo(Player player){
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		return new ClickableBuilder(
				Material.PLAYER_HEAD,
				meta->{
					meta.setPlayerProfile(player.getPlayerProfile());
					TranslationKey name;
					TranslationKey description;
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.add("player", player.name());

					Faction faction;
					if (fPlayer.getFactionId() != null){
						faction = unity.getFactionManager().get(fPlayer.getFactionId());
						placeholders.add("faction", faction.getName());
						placeholders.add("faction_id", faction.getUniqueId().toString());
						placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
						FactionMember member = faction.getMember(player.getUniqueId());
						placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));

						name = Translations.GUI_BUTTON_USER_INFO_FACTION_NAME;
						description = Translations.GUI_BUTTON_USER_INFO_FACTION_DESCRIPTION;
					} else {
						name = Translations.GUI_BUTTON_USER_INFO_NO_FACTION_NAME;
						description = Translations.GUI_BUTTON_USER_INFO_NO_FACTION_DESCRIPTION;
					}

					meta.displayName(component(player, name, placeholders));
					meta.lore(lore(player, description, placeholders));
				},
				SkullMeta.class
		);
	}

}
