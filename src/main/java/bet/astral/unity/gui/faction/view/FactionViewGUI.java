package bet.astral.unity.gui.faction.view;

import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.gui.core.RootGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class FactionViewGUI extends BaseGUI implements RootGUI {
	public FactionViewGUI(GUIHandler handler) {
		super(handler);
	}

	@Override
	public void openMainMenu(@NotNull Player player) {
		// Open main inventory view
	}
}