package bet.astral.unity.gui.no_faction;

import bet.astral.unity.gui.BaseGUI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class NoFactionBaseGUI extends BaseGUI {
	protected final NoFactionGUI root;
	public NoFactionBaseGUI(@NotNull NoFactionGUI root) {
		super(root);
		this.root = root;
	}
}
