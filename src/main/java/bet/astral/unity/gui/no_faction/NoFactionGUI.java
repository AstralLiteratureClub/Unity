package bet.astral.unity.gui.no_faction;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.unity.commands.no_faction.CreateSubCommand;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.gui.core.RootGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.messenger.Translations;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class NoFactionGUI extends BaseGUI implements RootGUI {
	public NoFactionGUI(GUIHandler handler) {
		super(handler);
	}

	@Override
	public void openMainMenu(Player player) {
		InventoryGUI.builder(3)
				.messenger(getMessenger())
				.placeholderGenerator(p -> {
					PlaceholderList placeholders = placeholders(p, null);
					placeholders.add(Placeholder.of("player", player.name()));
					return placeholders;
				})
				.title(Translations.GUI_NO_FACTION)
				.background(GUIBackgrounds.DARK)
				.addClickable(13, Clickable.builder(Material.OAK_SIGN).title(Translations.GUI_BUTTON_CREATE_FACTION_NAME).description(Translations.GUI_BUTTON_CREATE_FACTION_DESCRIPTION)
						.actionGeneral(action -> {
							new SignGUIBuilder()
									.setLines(splitComponent(player, Translations.SIGN_GUI_TEXT_CREATE).toArray(Component[]::new))
									.setHandler(() -> List.of(
											(p, result) -> {
												String name = plainText.serialize(result.getFirst());
												name = name.replace(" ", "_");
												CreateSubCommand.handle(player, name, true);
											}
									))
									.build()
									.open(player);
						})
				)
				.addClickable(22, createAccountInfo(player))
				.build()
				.open(player);
	}
}