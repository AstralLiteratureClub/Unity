package bet.astral.unity.alliance.gui;

import bet.astral.unity.Unity;
import bet.astral.unity.alliance.Alliance;
import bet.astral.unity.alliance.gui.faction.AFactionBaseGUI;
import bet.astral.unity.alliance.gui.no_faction.ANoFactionBaseGUI;
import bet.astral.unity.gui.GUIHandler;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class AGUIHandler extends GUIHandler {
	private final Unity unity;
	private final Alliance alliance;
	private final AFactionBaseGUI aFactionGUI;
	private final ANoFactionBaseGUI aNoFactionGUI;

	public AGUIHandler(Alliance alliance) {
		super(alliance.getUnity());
		this.alliance = alliance;
		this.unity = alliance.getUnity();
		this.aFactionGUI = new AFactionBaseGUI(this);
		aNoFactionGUI = new ANoFactionBaseGUI(this);
	}

	@Override
	public void openMainMenu(Player player) {
		UUID faction = unity.getPlayerManager().get(player.getUniqueId()).getFactionId();
		if (faction == null){
			aNoFactionGUI.openMainMenu(player);
		} else{
			aFactionGUI.openMainMenu(player);
		}
	}
}
