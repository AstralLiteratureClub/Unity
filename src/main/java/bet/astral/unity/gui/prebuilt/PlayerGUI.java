package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.more4j.tuples.Triplet;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerGUI extends BaseGUI implements PrebuiltGUI<Triplet<List<? extends OfflinePlayer>, Integer, Faction>> {
	@NotNull
	private final TranslationKey title;
	@NotNull
	private final TranslationKey headName;
	@NotNull
	private final TranslationKey headLore;
	@NotNull
	private final TranslationKey previousPageName;
	@NotNull
	private final TranslationKey previousPageLore;
	@NotNull
	private final TranslationKey nextPageName;
	@NotNull
	private final TranslationKey nextPageLore;
	@NotNull
	private final TranslationKey returnName;
	@NotNull
	private final TranslationKey returnLore;
	@Range(from=2, to=6)
	private final int maxRows;
	@NotNull
	private final BiConsumer<Player, OfflinePlayer> clickConsumer;
	@NotNull
	private final Consumer<Player> returnConsumer;
	@Nullable
	private final Consumer<Player> openConsumer;
	@Nullable
	private final Consumer<Player> closeConsumer;

	public PlayerGUI(@NotNull BaseGUI guiHandler,
	                 @NotNull Translation title,
	                 @NotNull Translation headName, @NotNull Translation headLore,
	                 @NotNull Translation previousPageName, @NotNull Translation previousPageLore,
	                 @NotNull Translation nextPageName, @NotNull Translation nextPageLore,
	                 @NotNull Translation returnName, @NotNull Translation returnLore,
	                 @Range(from = 2, to = 6) int maxRows,
	                 @NotNull BiConsumer<Player, OfflinePlayer> clickConsumer,
	                 @NotNull Consumer<Player> returnConsumer,
	                 @Nullable Consumer<Player> openConsumer,
	                 @Nullable Consumer<Player> closeConsumer) {
		super(guiHandler);
		this.title = title;
		this.headName = headName;
		this.headLore = headLore;
		this.previousPageName = previousPageName;
		this.previousPageLore = previousPageLore;
		this.nextPageName = nextPageName;
		this.nextPageLore = nextPageLore;
		this.returnName = returnName;
		this.returnLore = returnLore;
		this.maxRows = maxRows;
		this.clickConsumer = clickConsumer;
		this.returnConsumer = returnConsumer;
		this.openConsumer = openConsumer;
		this.closeConsumer = closeConsumer;
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, Triplet<List<? extends OfflinePlayer>, Integer, Faction> data, PlaceholderList placeholdersList) {
		final int maxSlots = maxRows*9;
		final List<? extends OfflinePlayer> players = data.getFirst();
		final int currentPage = data.getSecond();
		final int playersSize = players.size();
		final Faction faction = data.getThird();
		int pages = 0;
		for (int i = 0; i < 100; i++){
			pages++;
			if (players.size()<(pages*maxSlots)){
				break;
			}
		}
		PlaceholderList placeholders = new PlaceholderList(placeholdersList);
		placeholders.add("page", currentPage+1);
		placeholders.add("previous_page", currentPage);
		placeholders.add("next_page", currentPage+2);
		placeholders.add("pages", pages);

		InventoryGUIBuilder builder = InventoryGUI.builder(5)
				.messenger(getMessenger())
				.placeholderGenerator(p->placeholders)
				.title(title)
				.background(GUIBackgrounds.PLAYERS);
		int form = currentPage == 0 ? 0 : (currentPage-1)*maxSlots;
		int to = currentPage == 0 ? maxSlots : currentPage*maxSlots;
		boolean lastPage;
		boolean firstPage = currentPage == 0;
		if (to > playersSize) {
			to = playersSize;
			lastPage = true;
		} else {
			lastPage = false;
		}

		int i = 0;

		List<? extends OfflinePlayer> subList = players.subList(form, to);

		for (OfflinePlayer offlinePlayer : subList) {
			builder.addClickable(i, Clickable.builder(Material.PLAYER_HEAD, meta -> {
						meta.setPlayerProfile(offlinePlayer.getPlayerProfile());
					}, SkullMeta.class)
							.placeholderGenerator(p->{
								PlaceholderList playerPlaceholders = new PlaceholderList();
								playerPlaceholders.addAll(placeholders);
								playerPlaceholders.removeIf(pl->pl.getKey().equalsIgnoreCase("player"));
								playerPlaceholders.add("player", Objects.requireNonNull(offlinePlayer.getName()));
								return playerPlaceholders;
							})
							.title(headName)
							.description(headLore)
					.actionGeneral((clickable, itemStack, player1) -> clickConsumer.accept(player1, offlinePlayer))
					.priority(100)
			);
			i++;
		}

		builder
				.addClickable(maxSlots-9, Clickable.builder(Material.ARROW)
						.placeholderGenerator(p->placeholders)
						.title(previousPageName)
						.description(previousPageLore)
						.permission(bet.astral.guiman.permission.Permission.of(p -> !firstPage))
						.actionGeneral((clickable, itemStack, player1) -> openData(player1, data.cloneAsMutable().setSecond(currentPage-1)))
						.priority(100)
				)
				.clickable(maxSlots-5, Clickable.builder(Material.BARRIER)
						.placeholderGenerator(p->placeholders)
						.title(returnName)
						.description(returnLore)
						.actionGeneral((clickable, itemStack, player1) -> returnConsumer.accept(player1)))

				.addClickable(maxSlots-1, Clickable.builder(Material.ARROW)
						.placeholderGenerator(p->placeholders)
						.title(nextPageName)
						.description(nextPageLore)
						.permission(bet.astral.guiman.permission.Permission.of(p -> !lastPage))
						.actionGeneral((clickable, itemStack, player1) -> openData(player1, data.cloneAsMutable().setSecond(currentPage+1)))
						.priority(100)
				);

		builder.openConsumer(openConsumer).closeConsumer(closeConsumer);

		return builder;
	}
}
