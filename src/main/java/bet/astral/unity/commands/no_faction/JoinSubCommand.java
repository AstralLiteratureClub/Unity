package bet.astral.unity.commands.no_faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.commands.arguments.JoinableFactionParser;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class JoinSubCommand extends UnityCommand {
	public JoinSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot, "join", Translations.COMMAND_NO_FACTION_JOIN_FACTION_DESCRIPTION, p->
				p.permission(requireNoFaction("join"))
						.required(JoinableFactionParser.joinableComponent().name("faction"))
						.senderType(Player.class)
						.handler(context->{
							unity().getFactionMethods().join(context.sender(), context.get("faction"), null);
						})
				, "accept-invite").register();
	}
}
