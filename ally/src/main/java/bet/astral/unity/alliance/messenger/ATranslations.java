package bet.astral.unity.alliance.messenger;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.unity.messenger.Translations;

import static bet.astral.messenger.v2.translation.Translation.text;

public class ATranslations extends Translations {
	public static final Translation COMMAND_ALLIANCE_DESCRIPTION = new Translation("commands.alliance.description").add(ComponentType.CHAT, text("The root command for alliances"));
}
