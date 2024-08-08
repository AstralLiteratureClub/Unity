package bet.astral.unity.gui.faction;

import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.gui.RootGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.messenger.Translations;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class FactionGUI extends BaseGUI implements RootGUI {
	private final GUIHandler guiHandler;
	private final MembersGUI membersGUI;
	private final InviteGUI inviteGUI;
	public FactionGUI(GUIHandler guiHandler) {
		super(guiHandler);
		this.guiHandler = guiHandler;
		this.membersGUI = new MembersGUI(this);
		this.inviteGUI = new InviteGUI(this);
	}

	@ApiStatus.Internal
	public void openMainMenu(Player player) {
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
		assert faction != null;
		new InventoryGUIBuilder(5)
				.messenger(getMessenger())
				.title(component(player, Translations.GUI_FACTION, Placeholder.of("player", player.name()), Placeholder.of("faction", faction.getName())))
				.background(GUIBackgrounds.MAIN_MENU)
				.addClickable(10, randomMember(player, faction, Translations.GUI_BUTTON_FACTION_MEMBERS_NAME, Translations.GUI_BUTTON_FACTION_MEMBERS_DESCRIPTION)
						.actionGeneral(((clickable, itemStack, player1) -> membersGUI.openMembers(player1, faction))))
				.addClickable(28, randomPlayer(player, faction.getInvitablePlayers(), Translations.GUI_BUTTON_FACTION_INVITE_NAME, Translations.GUI_BUTTON_FACTION_INVITE_DESCRIPTION)
						.actionGeneral((clickable, itemStack, player1) -> inviteGUI.openInvitable(player1)))
				.addClickable(29, randomPlayer(player, faction.getInvitablePlayers(), Translations.GUI_BUTTON_FACTION_INVITE_NAME, Translations.GUI_BUTTON_FACTION_INVITE_DESCRIPTION)
						.actionGeneral((clickable, itemStack, player1) -> inviteGUI.openSentInvites(player1)))
				.addClickable(40, createAccountInfo(player))
				.build()
				.open(player);
	}
}