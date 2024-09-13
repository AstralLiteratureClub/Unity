package bet.astral.unity.commands.debug;

import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class DebugCommand extends UnityCommand {
	protected static RegistrableCommand<? extends CommandSender> debugRoot;
	public DebugCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}
	public void init(){
		debugRoot = command(factionRoot, "debug", Translations.COMMAND_FACTION_DEBUG_DESCRIPTION, b->b.permission("unity.debug"));
		debugRoot.register();
	}
}
