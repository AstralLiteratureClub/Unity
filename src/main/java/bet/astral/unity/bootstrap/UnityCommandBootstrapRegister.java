package bet.astral.unity.bootstrap;

import bet.astral.cloudplusplus.paper.bootstrap.BootstrapCommandRegisterer;
import bet.astral.cloudplusplus.paper.bootstrap.BootstrapHandler;
import bet.astral.cloudplusplus.paper.mapper.CommandSourceStackToCommandSenderMapper;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.unity.messenger.UnityMessenger;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnityCommandBootstrapRegister implements BootstrapCommandRegisterer<CommandSender> {
	private final PaperCommandManager.Bootstrapped<CommandSender> commandManager;
	private final UnityMessenger messenger;
	private final Logger logger = LoggerFactory.getLogger("Unity/CommandRegisterer");
	private final BootstrapHandler handler;

	public UnityCommandBootstrapRegister(UnityMessenger messenger, BootstrapHandler afterBootstrap, BootstrapContext context) {
		this.commandManager = PaperCommandManager.builder(new CommandSourceStackToCommandSenderMapper()).executionCoordinator(ExecutionCoordinator.asyncCoordinator()).buildBootstrapped(context);
		this.messenger = messenger;
		this.handler = afterBootstrap;
	}

	@Override
	@NotNull
	public PaperCommandManager.Bootstrapped<CommandSender> getCommandManager() {
		return commandManager;
	}

	@Override
	public Logger getSlf4jLogger() {
		return logger;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	@Override
	public Receiver convertToReceiver(@NotNull CommandSender c) {
		return messenger.convertReceiver(messenger);
	}

	@Override
	public boolean isDebug() {
		return false;
	}

	@Override
	public @NotNull BootstrapHandler getHandler() {
		return handler;
	}
}
