package bet.astral.unity.gui.prebuilt.confirm;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.signman.SignGUI;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.prebuilt.PrebuiltGUI;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
public class SignConfirmGUI extends ConfirmGUI implements PrebuiltGUI<Object> {
	private final SignGUI gui;
	private final Material signMaterial;
	private final TranslationKey confirmSignItemName;
	private final TranslationKey confirmSignItemLore;

	public SignConfirmGUI(@NotNull BaseGUI base, @NotNull Translation menuName,
	                      @NotNull Translation confirmItemName, @NotNull Translation confirmItemLore,
	                      @NotNull Translation confirmSignItemName, @NotNull Translation confirmSignItemLore,
	                      @NotNull Translation cancelItemName, @NotNull Translation cancelItemLore,
	                      @NotNull Translation returnItemName, @NotNull Translation returnItemLore,
	                      @NotNull Consumer<Player> normalConfirmConsumer,
	                      @NotNull SignGUI signConfirm,
	                      @NotNull Consumer<Player> cancelConsumer,
	                      @NotNull Consumer<Player> returnConsumer,
	                      @Nullable Consumer<Player> openConsumer,
	                      @Nullable Consumer<Player> closeConsumer) {
		super(base, menuName,
				confirmItemName, confirmItemLore,
				cancelItemName, cancelItemLore,
				returnItemName, returnItemLore,
				normalConfirmConsumer,
				cancelConsumer,
				returnConsumer,
				openConsumer,
				closeConsumer);
		this.gui = signConfirm;
		signMaterial = signConfirm.getMaterial().getMaterial(signConfirm.getSignSize());
		this.confirmSignItemName = confirmSignItemName;
		this.confirmSignItemLore = confirmSignItemLore;
	}

	public SignConfirmGUI(@NotNull BaseGUI guiHandler, @NotNull Translation menuName,
	                      @NotNull Translation confirmItemName, @NotNull Translation confirmItemLore,
	                      @NotNull Translation confirmSignItemName, @NotNull Translation confirmSignItemLore,
	                      @NotNull Translation cancelItemName, @NotNull Translation cancelItemLore,
	                      @NotNull Translation returnItemName, @NotNull Translation returnItemLore,
	                      @NotNull Consumer<Player> normalConfirmConsumer,
	                      @NotNull SignGUIBuilder signConfirm,
	                      @NotNull Consumer<Player> cancelConsumer,
	                      @NotNull Consumer<Player> returnConsumer,
	                      @Nullable Consumer<Player> openConsumer,
	                      @Nullable Consumer<Player> closeConsumer) {
		this(guiHandler, menuName,
				confirmItemName, confirmItemLore,
				confirmSignItemName, confirmSignItemLore,
				cancelItemName, cancelItemLore,
				returnItemName, returnItemLore,
				normalConfirmConsumer,
				signConfirm.build(),
				cancelConsumer,
				returnConsumer,
				openConsumer,
				closeConsumer);
	}

	@Override
	public void open(Player player, PlaceholderList placeholders) {
		generateGUI(player, null, placeholders).build().open(player);
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, Object obj, PlaceholderList placeholders) {
		ClickableBuilder confirm = Clickable.builder(gui.getMaterial().getMaterial(gui.getSignSize()))
				.placeholderGenerator(p->placeholders)
				.title(confirmSignItemName)
				.description(confirmSignItemLore)
				.actionGeneral((action) -> gui.open(action.getWho()))
				.priority(100);

		return super.generateGUI(player, obj, placeholders)
				.clickable(2, confirm)
				.clickable(3, confirm)
				;
	}
}
