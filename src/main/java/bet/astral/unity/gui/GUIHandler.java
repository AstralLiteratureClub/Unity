package bet.astral.unity.gui;

import bet.astral.guiman.Clickable;
import bet.astral.guiman.ClickableBuilder;
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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class GUIHandler implements MessageSender.Packed {
	protected final Unity unity;
	protected final MiniMessage miniMessage = MiniMessage.miniMessage();
	protected final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
	protected final Clickable BACKGROUND_DARK = new ClickableBuilder(Material.GRAY_STAINED_GLASS_PANE, meta -> {
		meta.setHideTooltip(true);
		meta.displayName(null);
	}).build();
	protected final Clickable BACKGROUND_LIGHT = new ClickableBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, meta -> {
		meta.setHideTooltip(true);
		meta.displayName(null);
	}).build();
	public GUIHandler(@NotNull GUIHandler guiHandler){
		this.unity = guiHandler.unity;
	}
	public GUIHandler(Unity unity){
		this.unity = unity;
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

	protected PlaceholderList placeholders(@NotNull OfflinePlayer player, @Nullable Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.getName());
		if (faction != null) {
			placeholders.add("faction", faction.getName());
			placeholders.add("faction_id", faction.getUniqueId().toString());
			placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
			FactionMember member = faction.getMember(player.getUniqueId());
			placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));
		} else {
			placeholders.add("faction", "none");
			placeholders.add("faction_id", "none");
			placeholders.add("faction_first_created", "never");
			placeholders.add("faction_first_joined", "never");
		}
 		return placeholders;
	}

	public void overrideToolTooltips(@NotNull ItemMeta meta){
		meta.addItemFlags(ItemFlag.values());
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
					FactionMember randomMember = faction.getMember(uniqueId);
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
					meta.setPlayerProfile(offlinePlayer.getPlayerProfile());

					PlaceholderList placeholders = placeholders(player, faction);

					meta.displayName(component(player, name, placeholders));
					meta.lore(lore(player, description, placeholders));
				},
				SkullMeta.class
		);
	}
}
