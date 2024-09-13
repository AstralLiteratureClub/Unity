package bet.astral.unity.module;

import bet.astral.unity.Unity;
import bet.astral.unity.module.bootstrap.SubModuleCommandBootstrapInfo;
import org.jetbrains.annotations.NotNull;

public interface SubModuleBootstrap<T extends SubModule> {
	@NotNull
	SubModuleCommandBootstrapInfo getCommandBootstrap();
	@NotNull
	T create(Unity unity);
}
