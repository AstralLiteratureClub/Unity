package bet.astral.unity.alliance.commands;

import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;


public abstract class ACommand extends UnityCommand {
	protected static RegistrableCommand<? extends CommandSender> allianceRoot;
	public ACommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}
	public abstract void registerCommands();
}
