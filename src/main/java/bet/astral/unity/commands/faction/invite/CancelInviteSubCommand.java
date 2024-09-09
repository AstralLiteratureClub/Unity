package bet.astral.unity.commands.faction.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.commands.arguments.InvitableParser;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class CancelInviteSubCommand extends UnityCommand {
	public CancelInviteSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot, "cancel-invite", Translations.COMMAND_FACTION_INVITE_DESCRIPTION, b -> b
						.permission(requireFaction("unity.cancel-invite").and(Permission.CANCEL_INVITE))
						.optional("who", InvitableParser.invitableParser())
						.senderType(Player.class)
						.handler(context -> {
							Player player = context.sender();
							Player select = context.getOrDefault("who", null);
							unity().getFactionMethods()
									.cancelInvite(player, select, null);
						})).register();
	}
}