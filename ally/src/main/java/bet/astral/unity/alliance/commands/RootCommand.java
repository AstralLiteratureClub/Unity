package bet.astral.unity.alliance.commands;

import bet.astral.unity.alliance.Alliance;
import bet.astral.unity.alliance.messenger.ATranslations;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.module.BuiltInModules;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

public class RootCommand extends ACommand {
	public RootCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	public void registerCommands(){
		allianceRoot = command(factionRoot, "alliance", ATranslations.COMMAND_ALLIANCE_DESCRIPTION, b->
				b.permission("unity.root")
						.handler(context->{
							((Alliance) BuiltInModules.ALLIANCE).getGuiHandler().openMainMenu((Player) context.sender());
						}), "a", "ally");
		factionRoot.register();
	}
}