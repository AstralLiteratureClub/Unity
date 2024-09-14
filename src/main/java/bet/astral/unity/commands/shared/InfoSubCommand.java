package bet.astral.unity.commands.shared;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class InfoSubCommand extends UnityCommand {
	public InfoSubCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void registerCommands() {
		RegistrableCommand<? extends CommandSender> root = command(factionRoot, "info", Translations.COMMAND_FACTION_INFO_DESCRIPTION, b->
				b.permission("unity.info"));
		command(root, "faction", Translations.COMMAND_FACTION_INFO_FACTION_DESCRIPTION, b->
				b.handler(context->{
					message(context.sender(), Translations.MESSAGE_INFO_FACTION_FRONT);
				})).register();
		command(root, "player", Translations.COMMAND_FACTION_INFO_FACTION_DESCRIPTION, b->
				b.handler(context->context.sender().sendMessage("hey"))).register();
	}
}
