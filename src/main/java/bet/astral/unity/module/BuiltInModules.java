package bet.astral.unity.module;

import bet.astral.unity.alliance.Alliance;

public class BuiltInModules {
	public static Alliance ALLIANCE;

	public static void fetch(ModuleManager moduleManager){
		ALLIANCE = moduleManager.getSubModule(Alliance.class);
	}
}
