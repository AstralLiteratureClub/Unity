package bet.astral.unity.commands.arguments;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Faction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
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

public class MemberParser implements ArgumentParser<CommandSender, OfflinePlayer>, SuggestionProvider<CommandSender> {
	private static Unity unity;
	private void fetchUnity(){
		if (unity == null){
			unity = Unity.getPlugin(Unity.class);
		}
	}

	@NotNull
	public static ParserDescriptor<CommandSender, OfflinePlayer> memberParser(){
		return ParserDescriptor.of(new MemberParser(), OfflinePlayer.class);
	}

	@NotNull
	public static CommandComponent.Builder<CommandSender, OfflinePlayer> memberComponent(){
		return CommandComponent.<CommandSender, OfflinePlayer>builder().parser(memberParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull OfflinePlayer> parse(@NonNull CommandContext<@NonNull CommandSender> commandContext, @NonNull CommandInput commandInput) {
		final String input = commandInput.readString();
		fetchUnity();

		if (!(commandContext.sender() instanceof Player player)){
			return ArgumentParseResult.failure(new IllegalArgumentException("Only players can use this command"));
		}

		if (unity.getPlayerManager().fromBukkit(player).getFactionId() == null){
			return ArgumentParseResult.failure(new IllegalArgumentException("Only players with a faction can use this command"));
		}

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
		if (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()){
			return ArgumentParseResult.failure(new OfflinePlayerParser.OfflinePlayerParseException(input, commandContext));
		}
		return ArgumentParseResult.success(offlinePlayer);
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

			return faction.getMembersAsOfflinePlayers().stream().map(p-> ComponentTooltipSuggestion.suggestion(player.getName(), player.displayName())).collect(Collectors.toSet());
		});
	}
}
