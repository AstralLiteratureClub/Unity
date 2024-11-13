package bet.astral.unity.commands.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.commands.UnityConfirmableCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class DeleteSubCommand extends UnityConfirmableCommand implements InitAfterBootstrap {
	public DeleteSubCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}



	@Override
	public void registerCommands() {
		/*
		command(factionRoot, "disband", Translations.COMMAND_FACTION_DELETE_DESCRIPTION, b->
				b.permission(requireHaveRole("unity.disband", FactionRole.OWNER))
						.senderType(Player.class)
						.handler(context->{
							Player player = context.sender();
							if (!tryConfirm(player)) {
								requestConfirm(player,
										Delay.of(30, TimeUnit.SECONDS),
										p->{
									unity().getFactionMethods().delete(player);
										},
										p->{},
										p->{
											getMessenger().message(p, Translations.MESSAGE_FACTION_DELETE_TIME_RAN_OUT);
										}
										);
								message(player, Translations.MESSAGE_FACTION_DELETE_BEGIN);
							}
						}), "delete").register();
		 */
	}

	@Override
	public void init() {
		getUnity().getLogger().severe("IS NULL: "+ (factionRoot==null));
	}
}
