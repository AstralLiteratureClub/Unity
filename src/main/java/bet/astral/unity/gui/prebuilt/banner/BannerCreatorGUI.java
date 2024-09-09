package bet.astral.unity.gui.prebuilt.banner;

import bet.astral.guiman.clickable.ClickAction;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.PrebuiltGUI;
import bet.astral.unity.gui.prebuilt.banner.data.BannerBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

// TODO Finish
public class BannerCreatorGUI extends BaseGUI implements PrebuiltGUI<BannerBuilder> {
	private final Map<UUID, DyeColor> selectColor = new HashMap<>();
	private final Translation title;
	private final Translation returnTitle;
	private final Translation returnDescription;
	private final ClickAction returnAction;
	private final Function<Player, Boolean> canUse;
	private final Consumer<Player> cannotUseConsumer;
	private final Consumer<Player> closeConsumer;
	private final Consumer<Player> openConsumer;
	public BannerCreatorGUI(@NotNull BaseGUI baseGUI, Translation title, Translation returnTitle, Translation returnDescription, ClickAction returnAction, Function<Player, Boolean> canUse, Consumer<Player> cannotUseConsumer, Consumer<Player> closeConsumer, Consumer<Player> openConsumer) {
		super(baseGUI);
		this.title = title;
		this.returnTitle = returnTitle;
		this.returnDescription = returnDescription;
		this.returnAction = returnAction;
		this.canUse = canUse;
		this.cannotUseConsumer = cannotUseConsumer;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, BannerBuilder bannerBuilder, PlaceholderList placeholder) {
		InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.SIX)
				.messenger(getMessenger())
				.placeholderGenerator(p->placeholder)
				.title(title)
				.background(GUIBackgrounds.BANNER_BUILDER)
				.clickable(47, Clickable.builder(Material.BARRIER).title(returnTitle).description(returnDescription).actionGeneral(returnAction))
				;
		List<ClickableLike> clickActions = new ArrayList<>();
		if (bannerBuilder.getBase()==null){
			for (DyeColor color : DyeColor.values()){
				ClickAction action = (context)->bannerBuilder.setBase(color);
				ClickableBuilder clickable = Clickable.builder(BannerBuilder.dyeToDye(color))
						.placeholderGenerator(p-> PlaceholderCollection.list(
								Placeholder.translation("dye_color", TranslationKey.of(("dye-colors"+color.name().replace("_", "-").toLowerCase())), ComponentType.CHAT)))
								.actionGeneral(action)
						.title(returnTitle)
						.description(returnDescription)
						;
				clickActions.add(clickable);
			}
		}
		return builder;
	}
}
