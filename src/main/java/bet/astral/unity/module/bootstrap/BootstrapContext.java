package bet.astral.unity.module.bootstrap;

import bet.astral.unity.messenger.UnityMessenger;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class BootstrapContext {
	private final UnityMessenger messenger;
	private final Path unityPath;

	public BootstrapContext(UnityMessenger messenger, Path unityPath) {
		this.messenger = messenger;
		this.unityPath = unityPath;
	}
}
