package bet.astral.unity.alliance;

import bet.astral.unity.Unity;
import bet.astral.unity.module.SubModuleBootstrap;
import bet.astral.unity.module.bootstrap.SubModuleCommandBootstrapInfo;
import org.jetbrains.annotations.NotNull;

public class AllianceBootstrap implements SubModuleBootstrap<Alliance> {
	private final SubModuleCommandBootstrapInfo commandBootstrapInfo = new SubModuleCommandBootstrapInfo();
	{
		commandBootstrapInfo.registerPackage("bet.astral.unity.alliance.commands");
	}
	@Override
	public @NotNull SubModuleCommandBootstrapInfo getCommandBootstrap() {
		return commandBootstrapInfo;
	}

	@Override
	public @NotNull Alliance create(Unity unity) {
		return new Alliance(unity, this);
	}
}
