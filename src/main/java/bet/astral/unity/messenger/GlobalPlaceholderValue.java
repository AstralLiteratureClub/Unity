package bet.astral.unity.messenger;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.manager.PlaceholderManager;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalPlaceholderValue {
	@NotNull
	String category();
	@NotNull
	String value();

	class InsertionHook {
		public static void insert(PlaceholderManager placeholderManager, Object object, String category) {
			new InsertionHook(placeholderManager, category, object);
		}
		private final Map<String, Field> fields = new HashMap<>();

		public InsertionHook(PlaceholderManager placeholderManager, String name, Object object) {
			getFields(name, object.getClass().getFields());
			getFields(name, object.getClass().getDeclaredFields());

			for (Map.Entry<String, Field> fieldEntry : fields.entrySet()){
				Placeholder placeholder = handleField(name.toLowerCase()+"_"+fieldEntry.getKey().toLowerCase(), object, fieldEntry.getValue());
				if (placeholder==null){
					continue;
				}
				placeholderManager.overridePlaceholder(placeholder);
			}
		}

		private void getFields(String name, Field[] fields){
			for (Field field : fields) {
				field.setAccessible(true);
				if (!field.isAnnotationPresent(GlobalPlaceholderValue.class)){
					continue;
				}
				GlobalPlaceholderValue placeholderValue = field.getAnnotation(GlobalPlaceholderValue.class);
				if (placeholderValue.category().equalsIgnoreCase(name)){
					this.fields.put(placeholderValue.value().toLowerCase(), field);
				}
			}
		}

		public @Nullable Placeholder handleField(String s, Object object, Field field) {
			Object value;
			try {
				value = field.get(object);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value instanceof Number number){
				return Placeholder.of(s, number);
			} else if (value instanceof ComponentLike componentLike){
				return Placeholder.of(s, componentLike.asComponent());
			} else if (value instanceof String string){
				return Placeholder.of(s, Component.text(string));
			} else if (value instanceof TranslationKey translationKey){
				return Placeholder.translation(s, translationKey, ComponentType.CHAT);
			}
			return Placeholder.of(s, Component.text(object.toString()));
		}
	}
}
