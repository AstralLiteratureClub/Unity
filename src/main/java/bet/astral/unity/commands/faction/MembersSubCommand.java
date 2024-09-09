package bet.astral.unity.commands.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class MembersSubCommand extends UnityCommand {
	public MembersSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot,
				"members",
				Translations.COMMAND_FACTION_MEMBERS_DESCRIPTION,
				b->b
						.permission(requireFaction("unity.members"))
						.senderType(Player.class)
						.handler(context->{
							unity().getGuiHandler().getFactionGUI().getMembersGUI().openMembers(context.sender());
						})
		);
	}
}
