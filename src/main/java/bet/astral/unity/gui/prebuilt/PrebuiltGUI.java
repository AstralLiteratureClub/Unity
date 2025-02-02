package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import org.bukkit.entity.Player;

import java.util.Arrays;

public interface PrebuiltGUI<O> {
	default void openData(Player player, O obj, Placeholder... placeholders){
		generateGUI(player, obj, new PlaceholderList(Arrays.stream(placeholders).toList())).build().open(player);
	}
	default void openData(Player player, O obj, PlaceholderList placeholders) {
		generateGUI(player, obj, placeholders).build().open(player);
	}

	default void open(Player player, Placeholder... placeholders){
		generateGUI(player, null, new PlaceholderList(Arrays.stream(placeholders).toList())).build().open(player);
	}
	default void open(Player player, PlaceholderList placeholders) {
		generateGUI(player, null, placeholders).build().open(player);
	}


	InventoryGUIBuilder generateGUI(Player player, O obj, PlaceholderList placeholder);
}
