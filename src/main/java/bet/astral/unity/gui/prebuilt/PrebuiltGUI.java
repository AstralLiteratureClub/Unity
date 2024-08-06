package bet.astral.unity.gui.prebuilt;

import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import org.bukkit.entity.Player;

import java.util.Arrays;

public interface PrebuiltGUI<O> {
	default void openData(Player player, O obj, Placeholder... placeholders){
		generateGUI(player, obj, new PlaceholderList(Arrays.stream(placeholders).toList()));
	}
	default void openData(Player player, O obj, PlaceholderList placeholders) {
		generateGUI(player, obj, placeholders).build().generateInventory(player);
	}

	default void open(Player player, Placeholder... placeholders){
		generateGUI(player, null, new PlaceholderList(Arrays.stream(placeholders).toList()));
	}
	default void open(Player player, PlaceholderList placeholders) {
		generateGUI(player, null, placeholders).build().generateInventory(player);
	}


	InventoryGUIBuilder generateGUI(Player player, O obj, PlaceholderList placeholder);
}
