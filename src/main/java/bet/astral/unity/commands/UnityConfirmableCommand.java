package bet.astral.unity.commands;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.commands.CPPBootstrapConfirmableCommand;
import bet.astral.messenger.v2.utils.MessageSender;
import bet.astral.messenger.v2.Messenger;
import bet.astral.unity.Unity;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public abstract class UnityConfirmableCommand extends CPPBootstrapConfirmableCommand<CommandSender> implements MessageSender.Packed, BaseCommand {
	private static Unity unity;
	protected final Messenger messenger;
	protected static RegistrableCommand<? extends CommandSender> factionRoot;
	public UnityConfirmableCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
		this.messenger = registerer.getMessenger();
		registerCommands();
	}

	public abstract void registerCommands();

	public static Unity unity(){
		if (unity==null){
			unity = Unity.getPlugin(Unity.class);
		}
		return unity;
	}

	@Override
	public Unity getUnity() {
		if (unity==null){
			unity = Unity.getPlugin(Unity.class);
		}
		return unity;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}
}

