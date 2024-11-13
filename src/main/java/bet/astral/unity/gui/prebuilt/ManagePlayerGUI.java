package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.more4j.tuples.Pair;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.core.CachedData;
import bet.astral.unity.gui.core.ClickablesProvider;
import bet.astral.unity.gui.core.PlayerExceptionHandler;
import bet.astral.unity.gui.core.data.PlayerCachedData;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public class ManagePlayerGUI extends BaseGUI implements PrebuiltGUI<OfflinePlayer> {
	private final TranslationKey title;
	private final TranslationKey returnTitle;
	private final TranslationKey returnDescription;
	private final ChestRows rows;
	private final Background background;
	private final Set<Pair<Integer, ClickablesProvider>> actions;
	private final BiConsumer<Player, OfflinePlayer> returnConsumer;
	private final BiConsumer<Player, OfflinePlayer> openConsumer;
	private final BiConsumer<Player, OfflinePlayer> closeConsumer;
	private final PlayerExceptionHandler exceptionHandler;

	public ManagePlayerGUI(@NotNull BaseGUI guiHandler,
	                       @NotNull TranslationKey title,
	                       @NotNull TranslationKey returnTitle, @NotNull TranslationKey returnDescription,
	                       @NotNull ChestRows rows,
	                       @NotNull Background background,
	                       @NotNull Set<Pair<Integer, ClickablesProvider>> actions,
	                       @NotNull BiConsumer<Player, OfflinePlayer> returnConsumer,
	                       @Nullable BiConsumer<Player, OfflinePlayer> openConsumer,
	                       @Nullable BiConsumer<Player, OfflinePlayer> closeConsumer, PlayerExceptionHandler exceptionHandler) {
		super(guiHandler);
		this.title = title;
		this.returnTitle = returnTitle;
		this.returnDescription = returnDescription;
		this.rows = rows;
		this.background = background;
		this.actions = actions;
		this.returnConsumer = returnConsumer;
		this.openConsumer = openConsumer;
		this.closeConsumer = closeConsumer;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, OfflinePlayer obj, PlaceholderList placeholder) {
		try {
			PlaceholderList placeholders = new PlaceholderList();
			placeholders.add("player", Objects.requireNonNull(obj.getName()));
			placeholders.addAll(placeholder);
			InventoryGUIBuilder builder = InventoryGUI.builder(rows)
					.messenger(getMessenger())
					.placeholderGenerator(p -> placeholders)
					.title(title)
					.openConsumer(p -> {
						if (openConsumer != null) openConsumer.accept(p, obj);
					})
					.closeConsumer(p -> {
						if (closeConsumer != null) closeConsumer.accept(p, obj);
					})
					.addClickable(rows.getSlots() - 5, Clickable.builder(Material.BARRIER).title(returnTitle).description(returnDescription)
							.actionGeneral((action) -> returnConsumer.accept(action.getWho(), obj)))
					.background(background);

			CachedData cachedData = new PlayerCachedData(obj, obj.getUniqueId(), unity.getPlayerManager().get(obj.getUniqueId()));

			for (Pair<Integer, ClickablesProvider> pair : actions) {
				int slot = pair.getFirst();
				ClickablesProvider clickablesProvider = pair.getSecond();
				List<ClickableLike> clickableLikes = clickablesProvider.provide(player, cachedData);
				builder.addClickable(slot, clickableLikes);
			}
			return builder;
		} catch (Exception e){
			exceptionHandler.accept(player);
			throw new RuntimeException(e);
		}
	}
}