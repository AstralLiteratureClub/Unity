package bet.astral.unity.commands.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.unity.commands.UnityCommand;
import bet.astral.unity.commands.UnityCommandBootstrapRegistrer;
import bet.astral.unity.data.FactionDatabase;
import bet.astral.unity.data.PlayerDatabase;
import bet.astral.unity.entity.Faction;
import bet.astral.unity.entity.FactionMember;
import bet.astral.unity.entity.FactionRole;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.messenger.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.UUID;

@Cloud
public class CreateSubCommand extends UnityCommand {
	public CreateSubCommand(UnityCommandBootstrapRegistrer registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}

	@Override
	public void init() {
		command(factionRoot, "create", Translations.COMMAND_FACTION_CREATE_DESCRIPTION, b->
				b
						.senderType(Player.class)
						.permission(requireNoFaction("unity.create"))
						.required(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("name").description(description(Translations.COMMAND_FACTION_CREATE_NAME_DESCRIPTION)))
						.handler(context->{
							Player p = context.sender();
							String name = context.get("name");
							handle(p, name, false);
						})
		).register();
	}

	public static void handle(Player p, String name, boolean openMenu){
		FactionManager factionManager = unity().getFactionManager();
		PlayerManager playerManager = unity().getPlayerManager();
		bet.astral.unity.entity.Player fPlayer = playerManager.fromBukkit(p);
		if (fPlayer.getFactionId() != null) {
			unity().getMessenger().message(p, Translations.MESSAGE_FACTION_REQUIRE_NO_FACTION);
			return;
		}

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("name", name);
		placeholders.add("player", p.name());
		if (name.isEmpty()) {
			unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_SIGN_NEED_NAME);
			return;
		}
		if (name.length() < unity().getConfiguration().getFactionCreateMinLength()) {
			unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_TOO_SHORT_NAME, placeholders);
			return;
		}
		if (name.length() > unity().getConfiguration().getFactionCreateMaxLength()) {
			unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_TOO_LONG_NAME, placeholders);
			return;
		}
		if (!name.matches(unity().getConfiguration().getFactionCreateAllowedChars())) {
			unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_INVALID_NAME, placeholders);
			return;
		}
		final String nameFinal = name;
		factionManager.exist(name).thenAccept(b->{
			if (b){
				unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_ALREADY_EXISTS, placeholders);
			} else {
				if (factionManager.isBanned(nameFinal)) {
					unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATE_BANNED_NAME, placeholders);
					return;
				}

				Faction faction = new Faction(UUID.randomUUID());
				faction.setName(nameFinal);
				FactionMember member = new FactionMember(unity().getMessenger(), p.getUniqueId());
				member.setRole(FactionRole.OWNER);
				faction.getMembers().put(member.getUniqueId(), member);

				factionManager.add(faction);
				fPlayer.setFactionId(faction.getUniqueId());

				PlayerDatabase playerDatabase = unity().getPlayerDatabase();
				playerDatabase.save(fPlayer);
				FactionDatabase factionDatabase = unity().getFactionDatabase();
				factionDatabase.save(faction);
				factionManager.saveCachedNames();

				unity().getMessenger().message(p, Translations.MESSAGE_FACTION_CREATED, placeholders);
				unity().getMessenger().broadcast(Translations.BROADCAST_FACTION_CREATED, placeholders);

				if (openMenu){
					unity().getFactionGUI().openFactionMenu(p);
				}
			}
		});
	}
}
