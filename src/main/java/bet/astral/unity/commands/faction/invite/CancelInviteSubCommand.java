package bet.astral.unity.commands.faction.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

@Cloud
public class CancelInviteSubCommand extends UnityCommand {
	public CancelInviteSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {

	}

	public static void handle(@NotNull Player player, @NotNull OfflinePlayer offlinePlayer){

	}
}
