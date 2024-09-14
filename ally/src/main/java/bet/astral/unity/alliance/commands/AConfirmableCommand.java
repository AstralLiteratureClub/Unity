package bet.astral.unity.alliance.commands;

import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.commands.UnityCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public abstract class AConfirmableCommand extends UnityCommand {
	protected static RegistrableCommand<? extends CommandSender> allianceRoot;
	public AConfirmableCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}
	public abstract void registerCommands();
}
