package bet.astral.unity.gui.prebuilt.confirm;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.PrebuiltGUI;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
public class ConfirmGUI extends BaseGUI implements PrebuiltGUI<Object> {
	@NotNull
	protected final TranslationKey menuName;
	@NotNull
	protected final TranslationKey confirmItemName;
	@NotNull
	protected final TranslationKey confirmItemLore;
	@NotNull
	protected final TranslationKey cancelItemName;
	@NotNull
	protected final TranslationKey cancelItemLore;
	@NotNull
	protected final TranslationKey returnItemName;
	@NotNull
	protected final TranslationKey returnItemLore;
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
		ClickableBuilder confirm = Clickable.builder(Material.GREEN_STAINED_GLASS_PANE, meta -> {
			meta.setCustomModelData(444444440);
		}).placeholderGenerator(p->placeholders).title(confirmItemName).description(confirmItemLore)
				.actionGeneral((clickable, itemStack, player1) -> confirmConsumer.accept(player1))
				.priority(10);

		ClickableBuilder cancel = Clickable.builder(Material.RED_STAINED_GLASS_PANE, meta -> {
					meta.setCustomModelData(444444441);
				}).placeholderGenerator(p->placeholders).title(cancelItemName).description(cancelItemLore)
				.actionGeneral((clickable, itemStack, player1) -> cancelConsumer.accept(player1))
				.priority(10);

		return InventoryGUI.builder(2)
				.messenger(getMessenger())
				.title(menuName)
				.placeholderGenerator(p->placeholders(player, null))
				.background(GUIBackgrounds.CONFIRM)
				.closeConsumer(closeConsumer)
				.openConsumer(openConsumer)
				.addClickable(0, confirm)
				.addClickable(1, confirm)
				.addClickable(2, confirm)
				.addClickable(3, confirm)
				.addClickable(5, cancel)
				.addClickable(6, cancel)
				.addClickable(7, cancel)
				.addClickable(8, cancel)
				.addClickable(13, Clickable.builder(Material.BARRIER)
						.placeholderGenerator(p->placeholders(player, null))
						.title(returnItemName)
						.description(returnItemLore)
						.actionGeneral((clickable, itemStack, player1) -> returnConsumer.accept(player1)));
	}
}
