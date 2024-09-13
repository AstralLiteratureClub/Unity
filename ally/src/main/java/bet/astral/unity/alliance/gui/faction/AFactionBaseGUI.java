package bet.astral.unity.alliance.gui.faction;

import bet.astral.unity.alliance.gui.ABaseGUI;
import bet.astral.unity.alliance.gui.AGUIHandler;
import bet.astral.unity.gui.core.RootGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AFactionBaseGUI extends ABaseGUI implements RootGUI {
	public AFactionBaseGUI(@NotNull AGUIHandler guiHandler) {
		super(guiHandler);
	}

	@Override
	public void openMainMenu(Player player) {

	}
}
