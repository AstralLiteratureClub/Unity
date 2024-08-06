package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.tuples.Triplet;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.GUIHandler;
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

public class PlayerGUI extends GUIHandler implements PrebuiltGUI<Triplet<List<? extends OfflinePlayer>, Integer, Faction>> {
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

	public PlayerGUI(@NotNull GUIHandler guiHandler,
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
				.name(component(player, title, placeholders))
				.setBackground(BACKGROUND_LIGHT)
				.setSlotClickable(maxSlots-8, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-7, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-6, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-5, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-4, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-3, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-2, BACKGROUND_DARK)
				.setSlotClickable(maxSlots-1, BACKGROUND_DARK);
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
			builder.addSlotClickable(i, new ClickableBuilder(Material.PLAYER_HEAD, meta -> {
						meta.setPlayerProfile(offlinePlayer.getPlayerProfile());
						PlaceholderList playerPlaceholders = new PlaceholderList(placeholders(offlinePlayer, faction));
						playerPlaceholders.addAll(placeholders);
						meta.displayName(component(player, headName, placeholders));
						meta.lore(lore(player, headLore, placeholders));
					}, SkullMeta.class)
							.setGeneralAction((clickable, itemStack, player1) -> clickConsumer.accept(player1, offlinePlayer))
			);
			i++;
		}

		builder
				.addSlotClickable(36, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, previousPageName, placeholders));
							meta.lore(lore(player, previousPageLore, placeholders));
						}).setPriority(10)
								.setPermission(bet.astral.guiman.permission.Permission.of(p -> !firstPage))
								.setDisplayIfNoPermissions(false)
				)
				.setSlotClickable(40, new ClickableBuilder(Material.BARRIER, meta -> {
					meta.displayName(component(player, returnName, placeholders));
					meta.lore(lore(player, returnLore, placeholders));
				}).setGeneralAction((clickable, itemStack, player1) -> returnConsumer.accept(player1)))

				.addSlotClickable(44, new ClickableBuilder(Material.ARROW, meta -> {
							meta.displayName(component(player, nextPageName, placeholders));
							meta.lore(lore(player, nextPageLore, placeholders));
						}).setPriority(10)
								.setPermission(bet.astral.guiman.permission.Permission.of(p -> !lastPage))
								.setDisplayIfNoPermissions(false)
				);

		builder.setOpenConsumer(openConsumer).setCloseConsumer(closeConsumer);

		return builder;
	}
}
