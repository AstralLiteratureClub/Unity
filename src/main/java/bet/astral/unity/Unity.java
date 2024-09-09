package bet.astral.unity;

import bet.astral.cloudplusplus.paper.bootstrap.BootstrapHandler;
import bet.astral.guiman.GUIMan;
import bet.astral.messenger.v2.MessageSender;
import bet.astral.messenger.v2.paper.PaperMessenger;
import bet.astral.more4j.event.EventManager;
import bet.astral.shine.Shine;
import bet.astral.signman.SignGUI;
import bet.astral.unity.data.Configuration;
import bet.astral.unity.data.FactionDatabase;
import bet.astral.unity.data.FactionInfoDatabase;
import bet.astral.unity.data.PlayerDatabase;
import bet.astral.unity.data.gson.GsonFactionDatabase;
import bet.astral.unity.data.gson.GsonPlayerDatabase;
import bet.astral.unity.gui.GUIHandler;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.managers.TickedManager;
import bet.astral.unity.messenger.GlobalPlaceholderValue;
import bet.astral.unity.messenger.UnityMessenger;
import bet.astral.unity.shared.FactionMethods;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Unity extends JavaPlugin implements MessageSender.Packed {
	private final UnityMessenger messenger;
	private Shine shine;
	@Getter(AccessLevel.NONE)
	private final BootstrapHandler bootstrapHandler;
	private final FactionDatabase factionDatabase = new GsonFactionDatabase(this);
	private final FactionInfoDatabase factionInfoDatabase = (GsonFactionDatabase) factionDatabase;
	private final PlayerDatabase playerDatabase = new GsonPlayerDatabase(this);
	private final FactionManager factionManager = new FactionManager(this);
	private final PlayerManager playerManager = new PlayerManager(this);
	private final GUIHandler guiHandler = new GUIHandler(this);
	private final Configuration configuration = new Configuration();
	private final EventManager eventManager = new EventManager();
	private final TickedManager tickedManager = new TickedManager(this);
	private final FactionMethods factionMethods;

	public Unity(UnityMessenger messenger, BootstrapHandler bootstrapHandler) {
		this.messenger = messenger;
		this.bootstrapHandler = bootstrapHandler;
		factionMethods = new FactionMethods(this);
	}

	@Override
	public void onEnable() {
		bootstrapHandler.init();
		shine = new Shine(this);
		PaperMessenger.init(this);
		GUIMan.init(this);
		SignGUI.init(this, true);

		GlobalPlaceholderValue.InsertionHook.insert(messenger.getPlaceholderManager(), configuration, "configuration");

		listener(playerManager);

		tickedManager.init();
	}

	@Override
	public void onDisable() {
		playerManager.saveAll();
		factionManager.saveAll();
	}

	private void listener(@NotNull Listener listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}
}
