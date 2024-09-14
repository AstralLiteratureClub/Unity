package bet.astral.unity.commands.debug;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.UUID;

@Cloud
public class DebugForceLoadCommand extends DebugCommand{
	public DebugForceLoadCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void registerCommands() {
		command(debugRoot, "loadplayer", Translations.COMMAND_FACTION_DEBUG_FORCE_LOAD, b->
				b.permission("unity.debug.load")
						.required(OfflinePlayerParser.offlinePlayerComponent().name("player"))
						.handler(context-> {
									CommandSender sender = context.sender();
									OfflinePlayer player = context.get("player");
									sender.sendMessage("Unloading...");
									unity().getPlayerManager().unload(player.getUniqueId());
									unity().getPlayerManager().getOrLoadAndCache(player.getUniqueId()).thenAccept(f -> {
										sender.sendMessage("Loaded player " + player.getName());
									});
								})).register();
		command(debugRoot, "loadfaction", Translations.COMMAND_FACTION_DEBUG_FORCE_LOAD, b->
				b.permission("unity.debug.load")
//						.required(UUIDParser.uuidComponent().name("faction"))
						.required(StringParser.stringComponent().name("faction"))
						.handler(context->{
							CommandSender sender = context.sender();
							UUID id = UUID.fromString(context.get("faction"));

							sender.sendMessage("Unloading...");
							unity().getFactionManager().unload(id);
							unity().getFactionManager().load(id).thenAccept(f->{
								if (f == null){
									sender.sendMessage("Couldn't find faction for id "+ id);
								} else {
									sender.sendMessage("Loaded faction "+ id);
								}
							});
						})).register();
	}
}
