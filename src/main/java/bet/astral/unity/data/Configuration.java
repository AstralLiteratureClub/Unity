package bet.astral.unity.data;

import bet.astral.unity.messenger.GlobalPlaceholderValue;
import lombok.Getter;

@Getter
public class Configuration {
	@GlobalPlaceholderValue(category = "configuration", value = "faction_name_regex")
	private String factionCreateAllowedChars = "[A-Za-z0-9_]*";
	@GlobalPlaceholderValue(category = "configuration", value = "faction_name_min_length")
	private int factionCreateMinLength = 3;
	@GlobalPlaceholderValue(category = "configuration", value = "faction_name_max_length")
	private int factionCreateMaxLength = 16;
}
