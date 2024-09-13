package bet.astral.unity.commands;

import bet.astral.cloudplusplus.paper.bootstrap.commands.CPPBootstrapCommand;
import bet.astral.messenger.v2.MessageSender;
import bet.astral.messenger.v2.Messenger;
import bet.astral.unity.Unity;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.UUID;

public class UnityCommand extends CPPBootstrapCommand<CommandSender> implements MessageSender.Packed {
	private static Unity unity;
	protected final Messenger messenger;
	protected static RegistrableCommand<? extends CommandSender> factionRoot;
	public UnityCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
		this.messenger = registerer.getMessenger();
		init();
	}

	public void init(){
		factionRoot = command("factions", Translations.COMMAND_FACTION_DESCRIPTION, b->
				b.permission("unity.root")
						.handler(context->{
							if (context.sender() instanceof Player player) {
								unity().getGuiHandler().openMainMenu(player);
							}
						}), "f", "faction");
		factionRoot.register();
	}

	public static Unity unity(){
		if (unity==null){
			unity = Unity.getPlugin(Unity.class);
		}
		return unity;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	public Permission requireNoFaction(String name) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			try {
				return unity().getPlayerManager().fromBukkit(player).getFactionId() == null;
			} catch (NullPointerException e){
				return true;
			}
		}));
	}
	public Permission requireFaction(String name) {
		return Permission.of(name).and(PredicatePermission.of(c->{
			if (!(c instanceof Player player)){
				return false;
			}
			UUID uniqueId = unity().getPlayerManager().fromBukkit(player).getFactionId();
			return uniqueId != null;
		}));
	}
}
