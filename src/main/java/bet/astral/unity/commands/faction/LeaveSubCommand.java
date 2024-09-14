package bet.astral.unity.commands.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.entity.FactionRole;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class LeaveSubCommand extends UnityCommand {
	public LeaveSubCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void registerCommands() {
		command(factionRoot, "leave", Translations.COMMAND_FACTION_LEAVE_DESCRIPTION, b->
				b.permission(requireNotHaveRole("unity.leave", FactionRole.OWNER))
						.senderType(Player.class)
						.handler(context->{
							unity().getFactionMethods().leave(context.sender(), null);
						}), "quit");
	}
}
