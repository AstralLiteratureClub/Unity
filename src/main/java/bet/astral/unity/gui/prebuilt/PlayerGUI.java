package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.tuples.Triplet;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerGUI extends BaseGUI implements PrebuiltGUI<Triplet<List<? extends OfflinePlayer>, Integer, Faction>> {
	@NotNull
	private final Translation title;
	@NotNull
	private final Translation headName;
	@NotNull
	private final Translation headLore;
	@NotNull
	private final Translation previousPageName;
	@NotNull
	private final Translation previousPageLore;
	@NotNull
	private final Translation nextPageName;
	@NotNull
	private final Translation nextPageLore;
	@NotNull
	private final Translation returnName;
	@NotNull
	private final Translation returnLore;
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

		InventoryGUIBuilder builder = new InventoryGUIBuilder(5)
				.messenger(getMessenger())
				.title(component(player, title, placeholders))
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
			builder.addClickable(i, new ClickableBuilder(Material.PLAYER_HEAD, meta -> {
						meta.setPlayerProfile(offlinePlayer.getPlayerProfile());
						PlaceholderList playerPlaceholders = new PlaceholderList(placeholders(offlinePlayer, faction));
						playerPlaceholders.addAll(placeholders);
						meta.displayName(component(player, headName, playerPlaceholders));
						meta.lore(lore(player, headLore, playerPlaceholders));
					}, SkullMeta.class)
					.actionGeneral((clickable, itemStack, player1) -> clickConsumer.accept(player1, offlinePlayer))
					.priority(100)
			);
			i++;
		}

		builder
				.addClickable(maxSlots-9, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, previousPageName, placeholders));
							meta.lore(lore(player, previousPageLore, placeholders));
						})
						.priority(10)
						.permission(bet.astral.guiman.permission.Permission.of(p -> !firstPage))
						.displayIfNoPermissions()
						.actionGeneral((clickable, itemStack, player1) -> openData(player1, data.cloneAsMutable().setSecond(currentPage-1)))
						.priority(100)
				)
				.clickable(maxSlots-5, new ClickableBuilder(Material.BARRIER, meta -> {
					meta.displayName(component(player, returnName, placeholders));
					meta.lore(lore(player, returnLore, placeholders));
				}).actionGeneral((clickable, itemStack, player1) -> returnConsumer.accept(player1)))

				.addClickable(maxSlots, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, nextPageName, placeholders));
							meta.lore(lore(player, nextPageLore, placeholders));
						})
						.priority(10)
						.permission(bet.astral.guiman.permission.Permission.of(p -> !lastPage))
						.displayIfNoPermissions()
						.actionGeneral((clickable, itemStack, player1) -> openData(player1, data.cloneAsMutable().setSecond(currentPage+1)))
						.priority(100)
				);

		builder.openConsumer(openConsumer).closeConsumer(closeConsumer);

		return builder;
	}
}
