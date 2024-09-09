package bet.astral.unity.gui.faction.own;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.gui.BaseGUI;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.gui.core.RootGUI;
import bet.astral.unity.gui.GUIBackgrounds;
import bet.astral.unity.gui.prebuilt.banner.BannerCreatorGUI;
import bet.astral.unity.gui.prebuilt.banner.data.BannerBuilder;
import bet.astral.unity.messenger.Translations;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class FactionGUI extends BaseGUI implements RootGUI {
	private final MembersGUI membersGUI;
	private final NonMembersGUI nonMembersGUI;
	private final BannerCreatorGUI bannerEditorGUI;
	public FactionGUI(GUIHandler guiHandler) {
		super(guiHandler);
		this.membersGUI = new MembersGUI(this);
		this.nonMembersGUI = new NonMembersGUI(this);
		this.bannerEditorGUI = new BannerCreatorGUI(this);
	}

	@ApiStatus.Internal
	public void openMainMenu(Player player) {
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
		assert faction != null;
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("player", player.name());
		placeholders.add("faction", faction.getName());
		InventoryGUI.builder(5)
				.messenger(getMessenger())
				.placeholderGenerator(p->placeholders)
				.title(Translations.GUI_FACTION)
				.background(GUIBackgrounds.MAIN_MENU)
				.addClickable(16, randomPlayer(player, faction.getInvitablePlayers(), Translations.GUI_BUTTON_FACTION_NON_MEMBERS_NAME, Translations.GUI_BUTTON_FACTION_NON_MEMBERS_DESCRIPTION)
						.actionGeneral((clickable, itemStack, player1) -> nonMembersGUI.openPlayers(player1)))
				.addClickable(25, randomMember(player, faction, Translations.GUI_BUTTON_FACTION_MEMBERS_NAME, Translations.GUI_BUTTON_FACTION_MEMBERS_DESCRIPTION)
						.actionGeneral(((clickable, itemStack, player1) -> membersGUI.openMembers(player1))))
				.addClickable(13, Clickable.general(ItemStack.of(Material.WHITE_BANNER), context->bannerEditorGUI.openData(player, new BannerBuilder())))
				.addClickable(40, createAccountInfo(player))
				.build()
				.open(player);
	}
}