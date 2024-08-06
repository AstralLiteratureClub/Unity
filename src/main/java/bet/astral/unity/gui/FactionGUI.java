package bet.astral.unity.gui;

import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.InventoryGUIBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.unity.Unity;
import bet.astral.unity.commands.faction.CreateSubCommand;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class FactionGUI extends GUIHandler {
	private final MembersGUI membersGUI;
	public FactionGUI(Unity unity) {
		super(unity);
		this.membersGUI = new MembersGUI(this);
	}

	public void openMainMenu(Player player) {
		Bukkit.getAsyncScheduler().runNow(unity, t -> {
			bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
			if (fPlayer.getFactionId() != null) {
				openFactionMenu(player);
			} else {
				openNoFactionMenu(player);
			}
		});
	}

	public void openFactionMenu(Player player) {
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
		assert faction != null;
		new InventoryGUIBuilder(5)
				.name(component(player, Translations.GUI_FACTION, Placeholder.of("player", player.name()), Placeholder.of("faction", faction.getName())))
				.setBackground(BACKGROUND_DARK)
				.addSlotClickable(10, randomMember(player, faction, Translations.GUI_BUTTON_MEMBERS_NAME, Translations.GUI_BUTTON_MEMBERS_DESCRIPTION)
						.setGeneralAction(((clickable, itemStack, player1) -> membersGUI.openMembers(player1, faction))))
				.addSlotClickable(40, createAccountInfo(player))
				.build()
				.generateInventory(player);
	}

	public void openNoFactionMenu(Player player) {
		new InventoryGUIBuilder(3)
				.name(component(player, Translations.GUI_NO_FACTION, Placeholder.of("player", player.name())))
				.setBackground(BACKGROUND_DARK)
				.addSlotClickable(13, new ClickableBuilder(Material.OAK_SIGN, meta -> {
							meta.displayName(component(player, Translations.GUI_BUTTON_CREATE_FACTION_NAME));
							meta.lore(lore(player, Translations.GUI_BUTTON_CREATE_FACTION_DESCRIPTION));
						})
								.setGeneralAction((clickable, itemStack, player1) -> {
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
				.addSlotClickable(22, createAccountInfo(player))
				.build()
				.generateInventory(player);
	}
	private @NotNull PlaceholderList placeholders(Player player, Faction faction) {
		return UnityMessenger.placeholders(player, faction);
	}

	private @NotNull ClickableBuilder createAccountInfo(Player player){
		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		return new ClickableBuilder(
				Material.PLAYER_HEAD,
				meta->{
					meta.setPlayerProfile(player.getPlayerProfile());
					TranslationKey name;
					TranslationKey description;
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.add("player", player.name());

					Faction faction;
					if (fPlayer.getFactionId() != null){
						faction = unity.getFactionManager().get(fPlayer.getFactionId());
						placeholders.add("faction", faction.getName());
						placeholders.add("faction_id", faction.getUniqueId().toString());
						placeholders.add(Placeholder.date("faction_first_created", faction.getFirstCreated()));
						FactionMember member = faction.getMember(player.getUniqueId());
						placeholders.add(Placeholder.date("faction_first_joined", member.getFirstJoined()));

						name = Translations.GUI_BUTTON_USER_INFO_FACTION_NAME;
						description = Translations.GUI_BUTTON_USER_INFO_FACTION_DESCRIPTION;
					} else {
						name = Translations.GUI_BUTTON_USER_INFO_NO_FACTION_NAME;
						description = Translations.GUI_BUTTON_USER_INFO_NO_FACTION_DESCRIPTION;
					}

					meta.displayName(component(player, name, placeholders));
					meta.lore(lore(player, description, placeholders));
				},
				SkullMeta.class
		);
	}
}