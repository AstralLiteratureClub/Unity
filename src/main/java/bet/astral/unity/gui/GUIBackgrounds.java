package bet.astral.unity.gui;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.StaticBackground;
import bet.astral.guiman.background.builders.BorderPatternBuilder;
import bet.astral.guiman.background.builders.PatternBackgroundBuilder;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.unity.Unity;
import bet.astral.unity.messenger.Translations;
import org.bukkit.Material;

public class GUIBackgrounds {
	public static final Clickable DARK_GLASS_PANE = new ClickableBuilder(Material.GRAY_STAINED_GLASS_PANE, meta -> {
		meta.setHideTooltip(true);
		meta.displayName(null);
	}).priority(0)
			.build();
	public static final Clickable LIGHT_GLASS_PANE = new ClickableBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, meta -> {
		meta.setHideTooltip(true);
		meta.displayName(null);
	}).priority(0).build();
	public static final Clickable COMING_SOON = new ClickableBuilder(Material.BARRIER)
			.title(Translations.GUI_BUTTON_COMING_SOON_NAME).description(Translations.GUI_BUTTON_COMING_SOON_LORE)
			.actionGeneral((clickable, itemStack, player) -> {
				Unity.getPlugin(Unity.class).message(player, Translations.MESSAGE_COMING_SOON);
			}).priority(0).build();

	public static final Background PLAYERS = new PatternBackgroundBuilder()
			.pattern("xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "yyyyyyyyy")
			.clickable('x', LIGHT_GLASS_PANE).clickable('y', DARK_GLASS_PANE)
			.build();
	public static final Background MAIN_MENU = new BorderPatternBuilder().rows(5)
			.border(DARK_GLASS_PANE)
			.nonBorder(LIGHT_GLASS_PANE)
			.leftBorder(false)
			.rightBorder(false)
			.build();
	public static final Background CONFIRM =
			new PatternBackgroundBuilder().pattern("xxxxyxxxx", "yyyyyyyyy")
					.clickable('x', LIGHT_GLASS_PANE).clickable('y', DARK_GLASS_PANE).build();

	public static final Background MANAGE = new PatternBackgroundBuilder()
			.pattern("xxxxxxxxx", "yyyyyyyyy").clickable('x', COMING_SOON).clickable('y', DARK_GLASS_PANE).build();

	public final Background DARK = new StaticBackground(DARK_GLASS_PANE);
	public final Background LIGHT = new StaticBackground(LIGHT_GLASS_PANE);
}