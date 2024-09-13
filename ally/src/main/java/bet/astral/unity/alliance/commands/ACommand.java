package bet.astral.unity.alliance.commands;

import bet.astral.unity.alliance.messenger.ATranslations;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import bet.astral.unity.module.BuiltInModules;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;


public class ACommand extends UnityCommand {
	protected static RegistrableCommand<? extends CommandSender> allianceRoot;
	public ACommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}
	public void init(){
		allianceRoot = command(factionRoot, "alliance", ATranslations.COMMAND_ALLIANCE_DESCRIPTION, b->
				b.permission("unity.root")
						.handler(context->{
							BuiltInModules.ALLIANCE.getGuiHandler().openMainMenu((org.bukkit.entity.Player) context.sender());
						}), "a", "ally");
		factionRoot.register();
	}
}
