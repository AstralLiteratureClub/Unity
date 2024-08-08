package bet.astral.unity.gui.faction;

import bet.astral.unity.gui.BaseGUI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class FactionBaseGUI extends BaseGUI {
	protected final FactionGUI root;
	public FactionBaseGUI(@NotNull FactionGUI root) {
		super(root);
		this.root = root;
	}
}
