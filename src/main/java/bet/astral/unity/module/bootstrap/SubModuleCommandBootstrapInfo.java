package bet.astral.unity.module.bootstrap;

import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

@Getter
public class SubModuleCommandBootstrapInfo {
	private final HashSet<String> packages = new HashSet<>();
	private final HashSet<Class<?>> classes = new HashSet<>();
	private boolean registered = false;
	public void registerPackage(@NotNull Package pkg){
		registerPackages(pkg);
	}
	public void registerPackages(@NotNull Package... pkgs){
		String[] strings = new String[pkgs.length];
		for (int i = 0; i < pkgs.length; i++){
			strings[i] = pkgs[i].getName();
		}
		registerPackages(strings);
	}

	public void registerPackage(@NotNull String pkg){
		registerPackages(pkg);
	}
	public void registerPackages(@NotNull String... pkg){
		if (isRegistered()){
			throw new IllegalStateException("Command bootstrap does not allow anymore new commands to be registered.");
		}
		packages.addAll(Arrays.asList(pkg));
	}
	public void registerClass(@NotNull Class<?> clazz){
		registerClasses(clazz);
	}
	public void registerClasses(@NotNull Class<?>... classes){
		if (isRegistered()){
			throw new IllegalStateException("Command bootstrap does not allow anymore new commands to be registered.");
		}
		this.classes.addAll(Arrays.stream(classes).toList());
	}

	public void registerClass(@NotNull String clazz){
		registerClasses(clazz);
	}
	public void registerClasses(@NotNull String... classes){
		Class<?>[] realClasses = new Class<?>[classes.length];
		for (int i = 0; i < classes.length; i++){
			try {
				realClasses[i] = Class.forName(classes[i]);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		registerClasses(realClasses);
	}

	@ApiStatus.Internal
	public void register(@NotNull UnityCommandBootstrapRegister commandBootstrapRegister) {
		for (String pkg : packages) {
			commandBootstrapRegister.registerCommands(pkg);
		}

		for (String clazz : packages) {
			try {
				commandBootstrapRegister.registerCommand(Class.forName(clazz));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
