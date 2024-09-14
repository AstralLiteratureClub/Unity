package bet.astral.unity.commands;

import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

public class RootCommand extends UnityCommand{
	public RootCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	public void registerCommands(){
		factionRoot = command("factions", Translations.COMMAND_FACTION_DESCRIPTION, b->
				b.permission("unity.root")
						.handler(context->{
							if (context.sender() instanceof Player player) {
								unity().getGuiHandler().openMainMenu(player);
							}
						}), "f", "faction");
		factionRoot.register();

		UnityConfirmableCommand.factionRoot = factionRoot;
	}
}
