package bet.astral.unity.module;

import bet.astral.unity.Unity;
import bet.astral.unity.alliance.Alliance;
import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModuleManager {
	private static final String[] BUILT_IN_ADDONS = {"bet.astral.unity.alliance"};
	private static final Map<String, SubModuleBootstrap<?>> bootstraps = new HashMap<>();
	private static final Map<String, SubModule> subModules = new HashMap<>();

	private void handle(@NotNull SubModuleBootstrap<?> bootstrap, Unity unity){
		SubModule subModule = bootstrap.create(unity);
		subModules.put(subModule.getClass().getName(), subModule);
		ModuleManager.bootstraps.remove(bootstrap.getClass().getName());
	}

	@ApiStatus.Internal
	public void enable(){
		for (SubModule subModule : subModules.values()){
			subModule.enable();
		}
	}
	@ApiStatus.Internal
	public void createInstances(Unity unity) {
		for (SubModuleBootstrap<?> bootstrap : bootstraps.values()){
			handle(bootstrap, unity);
		}
	}
	@ApiStatus.Internal
	public void bootstrap(){
		List<String> classes = new ArrayList<>(Arrays.stream(BUILT_IN_ADDONS).toList());

		for (String var : classes) {
			ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPaths(var).scan();

			for (Class<?> clazz : scanResult.getSubclasses(SubModuleBootstrap.class).loadClasses()){
				try {
					Constructor<?> constructor = clazz.getConstructor();
					SubModuleBootstrap<?> bootstrap = (SubModuleBootstrap<?>) constructor.newInstance();
					ModuleManager.bootstraps.put(bootstrap.getClass().getName(), bootstrap);
				} catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
				         IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}

			scanResult.close();
		}
	}
	@ApiStatus.Internal
	public void commands(UnityCommandBootstrapRegister commandBootstrapRegister){
		for (SubModuleBootstrap<?> bootstrap : bootstraps.values()){
			bootstrap.getCommandBootstrap().register(commandBootstrapRegister);
		}
	}

	public Set<SubModule> getSubModules(){
		return new HashSet<>(subModules.values());
	}


	public void disable(@NotNull SubModule subModule){
		subModule.disable();
	}

	public boolean isEnabled(@NotNull SubModule subModule){
		return subModule.enabled;
	}

	@Nullable
	public <T> T getSubModule(@NotNull Class<T> allianceClass) {
		//noinspection unchecked
		return (T) subModules.get(allianceClass.getName());
	}
}
