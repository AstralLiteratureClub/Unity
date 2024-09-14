package bet.astral.unity.module;

import bet.astral.unity.Unity;
import bet.astral.unity.module.bootstrap.SubModuleBootstrap;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.ApiStatus;

@Getter
public abstract class SubModule {
	private boolean hasCommands;
	private final Unity unity;
	protected boolean enabled;
	protected final ComponentLogger logger = ComponentLogger.logger("Unity/"+getName());

	protected SubModule(Unity unity, SubModuleBootstrap<?> subModuleBootstrap) {
		this.unity = unity;
		hasCommands = !subModuleBootstrap.getCommandBootstrap().getClasses().isEmpty() || subModuleBootstrap.getCommandBootstrap().getPackages().isEmpty() || subModuleBootstrap.getCommandBootstrap().getRootCommand() != null;
	}

	@ApiStatus.Internal
	protected void enable(){
		this.enabled = true;
	}
	@ApiStatus.Internal
	protected void disable(){
		this.enabled = false;
	}

	public abstract void onEnable();
	public abstract void onDisable();
	protected abstract String getName();
	protected abstract String[] getAuthors();

	public final ComponentLogger getLogger(){
		return logger;
	}
}
