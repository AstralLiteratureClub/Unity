package bet.astral.unity.alliance.messenger;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.unity.messenger.Translations;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import static bet.astral.messenger.v2.translation.Translation.text;

public class ATranslations {
	private static final Collection<Translation> translations = new HashSet<>();
	public static final Translation COMMAND_ALLIANCE_DESCRIPTION = new Translation("commands.alliance.description").add(ComponentType.CHAT, text("The root command for alliances"));

	/**
	 * Returns all translation keys used by unity.
	 * @return translations
	 */
	public static Collection<Translation> getTranslations(){
		init();
		return translations;
	}

	static void init(){
		Field[] fields = Translations.class.getFields();
		for (Field field : fields){
			try {
				if (!field.canAccess(null)){
					continue;
				}
				if (field.get(null) instanceof Translation translation){
					translations.add(translation);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
