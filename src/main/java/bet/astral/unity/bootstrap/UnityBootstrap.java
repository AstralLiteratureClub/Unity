package bet.astral.unity.bootstrap;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.BootstrapHandler;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.locale.LanguageTable;
import bet.astral.messenger.v2.locale.source.FileLanguageSource;
import bet.astral.messenger.v2.locale.source.LanguageSource;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.serializer.gson.TranslationGsonHelper;
import bet.astral.unity.Unity;
import bet.astral.unity.commands.RootCommand;
import bet.astral.unity.commands.debug.DebugCommand;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.messenger.UnityMessenger;
import bet.astral.unity.module.ModuleManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Initializes commands and messages of the plugin. Creates a new instance of {@link Unity}
 */
@Getter
public class UnityBootstrap implements PluginBootstrap {
	public static final boolean MODULES = false;
	public static final boolean ALWAYS_DELETE_MESSAGES = true;
	private final ModuleManager moduleManager = new ModuleManager();
	private UnityCommandBootstrapRegister commandRegistrer;
	private final BootstrapHandler afterBootstrap = new BootstrapHandler();
	private UnityMessenger messenger = new UnityMessenger();
	@Override
	public void bootstrap(@NotNull BootstrapContext context) {
		File file = new File(context.getDataDirectory().toFile(), "unity/messages/unity/en_us.json");
		if (file.exists() && ALWAYS_DELETE_MESSAGES){
			file.delete();
		}
		if (!file.exists()) {
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			FileWriter writer;
			try {
				writer = new FileWriter(file);
				Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
				writer.write(gson.toJson(TranslationGsonHelper.getDefaults(Translations.class, MiniMessage.miniMessage(), gson)));
				writer.flush();
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		LanguageSource source;
		try {
			source = FileLanguageSource.gson(messenger, Locale.US, file, MiniMessage.miniMessage());
			LanguageTable table = LanguageTable.of(source);
			messenger.registerLanguageTable(source.getLocale(), table);
			messenger.setDefaultLocale(source);
			messenger.setLocale(source.getLocale());
			messenger.loadTranslations(Translations.getTranslations().toArray(Translation[]::new));
			messenger.setPrefix(messenger.parseComponent(new MessageInfoBuilder(Translations.PREFIX).create(), ComponentType.CHAT));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		commandRegistrer = new UnityCommandBootstrapRegister(messenger, afterBootstrap, context);
		messenger.registerTo(commandRegistrer.getCommandManager());

		new RootCommand(commandRegistrer, commandRegistrer.getCommandManager());

		new DebugCommand(commandRegistrer, commandRegistrer.getCommandManager());

		commandRegistrer.registerCommands("bet.astral.unity.commands");
		if (MODULES) {
			bet.astral.unity.module.bootstrap.BootstrapContext bootstrapContext = new bet.astral.unity.module.bootstrap.BootstrapContext(messenger,
					context.getDataDirectory());
			moduleManager.bootstrap(bootstrapContext);
			moduleManager.commands(commandRegistrer);
		}
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
		Unity unity = new Unity(messenger, afterBootstrap, moduleManager);
		if (MODULES) {
			moduleManager.createInstances(unity);
		}
		return unity;
	}
}
