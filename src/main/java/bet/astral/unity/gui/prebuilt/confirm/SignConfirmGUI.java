package bet.astral.unity.gui.prebuilt.confirm;

import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.signman.SignGUI;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.unity.gui.GUIHandler;
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
	private final Translation confirmSignItemName;
	private final Translation confirmSignItemLore;

	public SignConfirmGUI(@NotNull GUIHandler guiHandler, @NotNull Translation menuName,
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
		super(guiHandler, menuName,
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
	public SignConfirmGUI(@NotNull GUIHandler guiHandler, @NotNull Translation menuName,
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
		generateGUI(player, null, placeholders).build().generateInventory(player);
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, Object obj, PlaceholderList placeholders) {
		ClickableBuilder confirm = new ClickableBuilder(gui.getMaterial().getMaterial(gui.getSignSize()), meta->{
			meta.setCustomModelData(444444440);
			meta.displayName(component(player, confirmSignItemName, placeholders));
			meta.lore(lore(player, confirmSignItemLore, placeholders));
		}).setGeneralAction((clickable, itemStack, player1) -> gui.open(player1));


		return super.generateGUI(player, obj, placeholders)
				.setSlotClickable(2, confirm)
				.setSlotClickable(3, confirm)
				;
	}
}
