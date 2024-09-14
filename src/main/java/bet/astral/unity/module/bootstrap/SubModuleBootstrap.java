package bet.astral.unity.module.bootstrap;

import bet.astral.unity.Unity;
import bet.astral.unity.module.SubModule;
import org.jetbrains.annotations.NotNull;

public interface SubModuleBootstrap<T extends SubModule> {
	@NotNull
	SubModuleCommandBootstrapInfo getCommandBootstrap();
	void bootstrap(BootstrapContext bootstrapContext);
	@NotNull
	T create(Unity unity);
}
