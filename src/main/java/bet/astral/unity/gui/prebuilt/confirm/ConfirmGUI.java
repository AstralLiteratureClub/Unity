package bet.astral.unity.gui.prebuilt.confirm;

import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.PrebuiltGUI;
import bet.astral.unity.messenger.Translations;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
public class ConfirmGUI extends BaseGUI implements PrebuiltGUI<Object> {
	@NotNull
	protected final Translation menuName;
	@NotNull
	protected final Translation confirmItemName;
	@NotNull
	protected final Translation confirmItemLore;
	@NotNull
	protected final Translation cancelItemName;
	@NotNull
	protected final Translation cancelItemLore;
	@NotNull
	protected final Translation returnItemName;
	@NotNull
	protected final Translation returnItemLore;
	@NotNull
	protected final Consumer<Player> confirmConsumer;
	@NotNull
	protected final Consumer<Player> cancelConsumer;
	@NotNull
	protected final Consumer<Player> returnConsumer;
	@Nullable
	protected final Consumer<Player> openConsumer;
	@Nullable
	protected final Consumer<Player> closeConsumer;

	public ConfirmGUI(@NotNull BaseGUI base,
	                  @NotNull Translation menuName,
	                  @NotNull Translation confirmItemName, @NotNull Translation confirmItemLore,
	                  @NotNull Translation cancelItemName, @NotNull Translation cancelItemLore,
	                  @NotNull Translation returnItemName, @NotNull Translation returnItemLore,
	                  @NotNull Consumer<Player> confirmConsumer,
	                  @NotNull Consumer<Player> cancelConsumer,
	                  @NotNull Consumer<Player> returnConsumer,
	                  @Nullable Consumer<Player> openConsumer,
	                  @Nullable Consumer<Player> closeConsumer) {
		super(base);
		this.menuName = menuName;
		this.confirmItemName = confirmItemName;
		this.confirmItemLore = confirmItemLore;
		this.cancelItemName = cancelItemName;
		this.cancelItemLore = cancelItemLore;
		this.returnItemName = returnItemName;
		this.returnItemLore = returnItemLore;
		this.confirmConsumer = confirmConsumer;
		this.cancelConsumer = cancelConsumer;
		this.returnConsumer = returnConsumer;
		this.openConsumer = openConsumer;
		this.closeConsumer = closeConsumer;
	}

	@Override
	public void open(Player player, PlaceholderList placeholders) {
		generateGUI(player, null, placeholders).build().open(player);
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, Object obj, PlaceholderList placeholders) {
		ClickableBuilder confirm = new ClickableBuilder(Material.GREEN_STAINED_GLASS_PANE, meta -> {
			meta.setCustomModelData(444444440);
			meta.displayName(component(player, confirmItemName, placeholders));
			meta.lore(lore(player, confirmItemLore, placeholders));
		}).actionGeneral((clickable, itemStack, player1) -> confirmConsumer.accept(player1));

		ClickableBuilder cancel = new ClickableBuilder(Material.RED_STAINED_GLASS_PANE, meta -> {
			meta.setCustomModelData(444444441);
			meta.displayName(component(player, cancelItemName, placeholders));
			meta.lore(lore(player, cancelItemLore, placeholders));
		}).actionGeneral((clickable, itemStack, player1) -> cancelConsumer.accept(player1));

		return new InventoryGUIBuilder(2)
				.messenger(getMessenger())
				.title(menuName)
				.placeholderGenerator(p->placeholders(player, null))
				.background(GUIBackgrounds.CONFIRM)
				.closeConsumer(closeConsumer)
				.openConsumer(openConsumer)
				.clickable(0, confirm)
				.clickable(1, confirm)
				.clickable(2, confirm)
				.clickable(3, confirm)
				.clickable(5, cancel)
				.clickable(6, cancel)
				.clickable(7, cancel)
				.clickable(8, cancel)
				.clickable(13, new ClickableBuilder(Material.BARRIER, meta -> {
					meta.displayName(component(player, returnItemName, placeholders));
					meta.lore(lore(player, returnItemLore, placeholders));
				})
						.actionGeneral((clickable, itemStack, player1) -> returnConsumer.accept(player1)));
	}
}
