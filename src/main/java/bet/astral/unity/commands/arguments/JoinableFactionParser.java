package bet.astral.unity.commands.arguments;

import bet.astral.messenger.v3.cloud.exceptions.TranslationException;
import bet.astral.messenger.v3.cloud.suggestion.MessengerTooltipSuggestion;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JoinableFactionParser implements ArgumentParser<CommandSender, Faction>, SuggestionProvider<CommandSender> {
	private static Unity unity;
	private void fetchUnity(){
		if (unity == null){
			unity = Unity.getPlugin(Unity.class);
		}
	}

	@NotNull
	public static ParserDescriptor<CommandSender, Faction> joinableParser(){
		return ParserDescriptor.of(new JoinableFactionParser(), Faction.class);
	}

	@NotNull
	public static CommandComponent.Builder<CommandSender, Faction> joinableComponent(){
		return CommandComponent.<CommandSender, Faction>builder().parser(joinableParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull Faction> parse(@NonNull CommandContext<@NonNull CommandSender> commandContext, @NonNull CommandInput commandInput) {
		final String input = commandInput.readString();
		fetchUnity();

		if (!(commandContext.sender() instanceof Player player)){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_CANNOT_USE_COMMAND));
		}

		if (unity.getPlayerManager().fromBukkit(player).getFactionId() == null){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_REQUIRE_FACTION));
		}

		bet.astral.unity.entity.Player fPlayer = unity.getPlayerManager().fromBukkit(player);
		if (fPlayer.getFactionId() != null){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_JOIN_FACTION_ALREADY_HAS_FACTION, Placeholder.of("faction", input)));
		}

		Faction faction = unity.getFactionManager().get(input);
		if (faction==null){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_UNKNOWN_FACTION, Placeholder.of("faction", input)));
		}
		if (faction.isBanned(player.getUniqueId())){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_JOIN_FACTION_BANNED, Placeholder.of("faction", input)));
		}

		if (faction.isPublic()) {
			return ArgumentParseResult.success(faction);
		}

		if (!faction.isInvited(player.getUniqueId())){
			return ArgumentParseResult.failure(new TranslationException(Faction.class, commandContext, Translations.ARGUMENT_JOIN_FACTION_NOT_INVITED, Placeholder.of("faction", input)));
		}

		return ArgumentParseResult.success(faction);
	}

	@Override
	public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<CommandSender> context, @NonNull CommandInput input) {
		return CompletableFuture.supplyAsync(()->{
			fetchUnity();

			if (!(context.sender() instanceof Player player)){
				return List.of();
			}

			if (unity.getPlayerManager().fromBukkit(player).getFactionId() != null){
				return List.of();
			}

			List<Faction> factions = new LinkedList<>(unity.getFactionManager().factions().stream().filter(f-> !f.isBanned(player.getUniqueId()) && (f.isPublic() || f.isInvited(player.getUniqueId()))).toList());
			return factions.stream().map(f-> MessengerTooltipSuggestion.of(f.getName(), unity.getMessenger(), new MessageInfoBuilder(Translations.ARGUMENT_FACTION).withReceiver(player).withPlaceholder(Placeholder.of("faction", f.getName())))).toList();
		});
	}
}
