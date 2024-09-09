package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.clickable.ClickContext;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.BaseGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FactionGUI extends BaseGUI implements PrebuiltGUI<Faction> {
	private final TranslationKey title;
	private final TranslationKey factionTitle;
	private final TranslationKey factionDescription;
	private final TranslationKey returnTitle;
	private final TranslationKey returnDescription;
	private final TranslationKey previousPageTitle;
	private final TranslationKey previousPageDescription;
	private final TranslationKey nextPageTitle;
	private final TranslationKey nextPageDescription;
	private final BiConsumer<ClickContext, Faction> factionClickConsumer;
	private final Consumer<Player> returnConsumer;
	private final Consumer<Player> openConsumer;
	private final Consumer<Player> closeConsumer;

	public FactionGUI(@NotNull BaseGUI baseGUI, TranslationKey title, TranslationKey factionTitle, TranslationKey factionDescription, TranslationKey returnTitle, TranslationKey returnDescription, TranslationKey previousPageTitle, TranslationKey previousPageDescription, TranslationKey nextPageTitle, TranslationKey nextPageDescription, BiConsumer<ClickContext, Faction> factionClickConsumer, Consumer<Player> returnConsumer, Consumer<Player> openConsumer, Consumer<Player> closeConsumer) {
		super(baseGUI);
		this.title = title;
		this.factionTitle = factionTitle;
		this.factionDescription = factionDescription;
		this.returnTitle = returnTitle;
		this.returnDescription = returnDescription;
		this.previousPageTitle = previousPageTitle;
		this.previousPageDescription = previousPageDescription;
		this.nextPageTitle = nextPageTitle;
		this.nextPageDescription = nextPageDescription;
		this.factionClickConsumer = factionClickConsumer;
		this.returnConsumer = returnConsumer;
		this.openConsumer = openConsumer;
		this.closeConsumer = closeConsumer;
	}

	@Override
	public InventoryGUIBuilder generateGUI(Player player, Faction obj, PlaceholderList placeholder) {
		return null;
	}
}
