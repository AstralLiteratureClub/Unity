package bet.astral.unity.gui.no_faction;

import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.unity.commands.no_faction.CreateSubCommand;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.gui.RootGUI;
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
		new InventoryGUIBuilder(3)
				.messenger(getMessenger())
				.title(component(player, Translations.GUI_NO_FACTION, Placeholder.of("player", player.name())))
				.background(GUIBackgrounds.MAIN_MENU)
				.addClickable(13, new ClickableBuilder(Material.OAK_SIGN, meta -> {
							meta.displayName(component(player, Translations.GUI_BUTTON_CREATE_FACTION_NAME));
							meta.lore(lore(player, Translations.GUI_BUTTON_CREATE_FACTION_DESCRIPTION));
						})
								.actionGeneral((clickable, itemStack, player1) -> {
									new SignGUIBuilder()
											.setLines(lore(player, Translations.SIGN_GUI_TEXT_CREATE).toArray(Component[]::new))
											.setHandler(()-> List.of(
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
