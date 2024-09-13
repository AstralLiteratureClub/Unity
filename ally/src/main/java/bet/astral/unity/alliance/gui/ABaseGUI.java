package bet.astral.unity.alliance.gui;

import bet.astral.unity.gui.BaseGUI;
import org.jetbrains.annotations.NotNull;

public class ABaseGUI extends BaseGUI {
	public ABaseGUI(@NotNull ABaseGUI baseGUI) {
		super(baseGUI);
	}

	public ABaseGUI(@NotNull AGUIHandler guiHandler) {
		super(guiHandler);
	}
}
