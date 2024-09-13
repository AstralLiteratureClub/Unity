package bet.astral.unity.alliance;

import bet.astral.unity.Unity;
import bet.astral.unity.alliance.gui.AGUIHandler;
import bet.astral.unity.module.SubModule;
import bet.astral.unity.module.SubModuleBootstrap;
import lombok.Getter;

@Getter
public class Alliance extends SubModule {
	private AGUIHandler guiHandler;
	protected Alliance(Unity unity, SubModuleBootstrap<?> subModuleBootstrap) {
		super(unity, subModuleBootstrap);
	}

	@Override
	public void onEnable() {
		guiHandler = new AGUIHandler(this);
	}

	@Override
	public void onDisable() {

	}

	@Override
	protected String getName() {
		return "Alliance";
	}

	@Override
	protected String[] getAuthors() {
		return new String[]{"Antritus"};
	}
}