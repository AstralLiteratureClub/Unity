package bet.astral.unity.alliance.bootstrap;

import bet.astral.messenger.v2.locale.LanguageTable;
import bet.astral.messenger.v2.locale.source.FileLanguageSource;
import bet.astral.messenger.v2.locale.source.LanguageSource;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.serializer.gson.TranslationGsonHelper;
import bet.astral.unity.Unity;
import bet.astral.unity.alliance.Alliance;
import bet.astral.unity.alliance.commands.RootCommand;
import bet.astral.unity.bootstrap.UnityBootstrap;
import bet.astral.unity.messenger.Translations;
import bet.astral.unity.module.bootstrap.BootstrapContext;
import bet.astral.unity.module.bootstrap.SubModuleBootstrap;
import bet.astral.unity.module.bootstrap.SubModuleCommandBootstrapInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

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
	public void bootstrap(BootstrapContext context) {
		File file = new File(context.getUnityPath().toFile(), "messages/alliance/en_us.json");
		if (file.exists() && UnityBootstrap.ALWAYS_DELETE_MESSAGES){
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
			source = FileLanguageSource.gson(context.getMessenger(), Locale.US, file, MiniMessage.miniMessage());
			LanguageTable table = context.getMessenger().getLanguageTable(Locale.US);
			table.addAdditionalLanguageSource(source);
			context.getMessenger().loadTranslations(Translations.getTranslations().toArray(Translation[]::new));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		commandBootstrapInfo.registerRootCommand(RootCommand.class);
		commandBootstrapInfo.registerPackages("bet.astral.unity.alliance.commands");
	}

	@Override
	public @NotNull Alliance create(Unity unity) {
		return new Alliance(unity, this);
	}
}
