package bet.astral.unity.commands.arguments;

import bet.astral.messenger.v2.cloud.exceptions.TranslationException;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.messenger.Translations;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InvitableParser implements ArgumentParser<CommandSender, Player>, SuggestionProvider<CommandSender> {
	private static Unity unity;
	private void fetchUnity(){
		if (unity == null){
			unity = Unity.getPlugin(Unity.class);
		}
	}

	@NotNull
	public static ParserDescriptor<CommandSender, Player> invitableParser(){
		return ParserDescriptor.of(new InvitableParser(), Player.class);
	}

	@NotNull
	public static CommandComponent.Builder<CommandSender, Player> invitableComponent(){
		return CommandComponent.<CommandSender, Player>builder().parser(invitableParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull Player> parse(@NonNull CommandContext<@NonNull CommandSender> commandContext, @NonNull CommandInput commandInput) {
		final String input = commandInput.readString();
		fetchUnity();

		if (!(commandContext.sender() instanceof Player player)){
			return ArgumentParseResult.failure(new TranslationException(Player.class, commandContext, Translations.ARGUMENT_CANNOT_USE_COMMAND));
		}

		if (unity.getPlayerManager().fromBukkit(player).getFactionId() == null){
			return ArgumentParseResult.failure(new TranslationException(Player.class, commandContext, Translations.ARGUMENT_REQUIRE_FACTION));
		}

		Player oPlayer = Bukkit.getPlayer(input);
		if (oPlayer == null){
			return ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext));
		}

		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		Faction faction = unity.getFactionManager().get(fPlayer.getFactionId());
		if (faction.isMember(oPlayer.getUniqueId())){
			return ArgumentParseResult.failure(new TranslationException(Player.class, commandContext, Translations.ARGUMENT_PLAYER_IN_SAME_FACTION, Placeholder.of("player", oPlayer.getName())));
		}
		if (faction.isInvited(oPlayer.getUniqueId())){
			return ArgumentParseResult.failure(new TranslationException(Player.class, commandContext, Translations.ARGUMENT_PLAYER_ALREADY_INVITED, Placeholder.of("player", oPlayer.getName())));
		}

		return ArgumentParseResult.success(oPlayer);
	}

	@Override
	public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<CommandSender> context, @NonNull CommandInput input) {
		return CompletableFuture.supplyAsync(()->{
			fetchUnity();

			if (!(context.sender() instanceof Player player)){
				return List.of();
			}

			if (unity.getPlayerManager().fromBukkit(player).getFactionId() == null){
				return List.of();
			}

			Faction faction = unity.getFactionManager().get(
					unity.getPlayerManager().fromBukkit(player).getFactionId());

			return faction.getInvitablePlayers().stream().map(p-> ComponentTooltipSuggestion.suggestion(player.getName(), player.displayName())).collect(Collectors.toSet());
		});
	}
}
