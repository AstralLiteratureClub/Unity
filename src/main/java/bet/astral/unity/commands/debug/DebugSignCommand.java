package bet.astral.unity.commands.debug;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.signman.*;
import bet.astral.unity.commands.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.List;

@Cloud
public class DebugSignCommand extends DebugCommand {
	private final PlainTextComponentSerializer plain = PlainTextComponentSerializer.plainText();
	public DebugSignCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	public void init() {
		command(debugRoot, "sign", Translations.COMMAND_FACTION_DEBUG_SIGN_DESCRIPTION, b -> b
				.permission("unity.debug.sign")
				.senderType(Player.class)
				.optional(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("0").defaultValue(DefaultValue.constant("0")))
				.optional(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("1").defaultValue(DefaultValue.constant("1")))
				.optional(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("2").defaultValue(DefaultValue.constant("2")))
				.optional(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("3").defaultValue(DefaultValue.constant("3")))
				.handler(context -> new SignGUIBuilder(plain)
						.setLine(0, (String) context.get("0"))
						.setLine(1, (String) context.get("1"))
						.setLine(2, (String) context.get("2"))
						.setLine(3, (String) context.get("3"))
						.setHandler(() -> List.of((player1, list) -> {
							player1.sendMessage(list.getFirst().clickEvent(ClickEvent.copyToClipboard(list.getFirstPlain())));
							player1.sendMessage(list.getSecond().clickEvent(ClickEvent.copyToClipboard(list.getSecondPlain())));
							player1.sendMessage(list.getThird().clickEvent(ClickEvent.copyToClipboard(list.getThirdPlain())));
							player1.sendMessage(list.getFourth().clickEvent(ClickEvent.copyToClipboard(list.getFourthPlain())));
						}))
						.build().open(context.sender()))).register();
	}
}