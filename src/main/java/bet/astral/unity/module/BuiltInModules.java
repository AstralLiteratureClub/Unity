package bet.astral.unity.module;

public class BuiltInModules {
	public static SubModule ALLIANCE;

	static void fetch(ModuleManager moduleManager){
		try {
			ALLIANCE = (SubModule) moduleManager.getSubModule(Class.forName("bet.astral.unity.alliance.Alliance"));
		} catch (ClassNotFoundException ignored) {}
	}
}
