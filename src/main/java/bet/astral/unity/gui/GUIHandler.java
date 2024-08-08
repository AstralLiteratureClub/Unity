package bet.astral.unity.gui;

import bet.astral.unity.Unity;
import bet.astral.unity.gui.faction.FactionGUI;
import bet.astral.unity.gui.no_faction.NoFactionGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class GUIHandler {
	private final Unity unity;
	private final FactionGUI factionGUI;
	private final NoFactionGUI noFactionGUI;

	public GUIHandler(Unity unity) {
		this.unity = unity;
		this.factionGUI = new FactionGUI(this);
		this.noFactionGUI = new NoFactionGUI(this);
	}

	public void openMainMenu(Player player) {
		Bukkit.getAsyncScheduler().runNow(unity, t -> {
			bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
			if (fPlayer.getFactionId() != null) {
				factionGUI.openMainMenu(player);
			} else {
				noFactionGUI.openMainMenu(player);
			}
		});
	}
}
