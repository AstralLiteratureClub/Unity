package bet.astral.unity.messenger;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.messenger.v2.translation.TranslationKey;
import com.sun.jna.platform.unix.solaris.LibKstat;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

import static bet.astral.messenger.v2.translation.Translation.text;

/**
 * Lists all translations used by unity and messenger
 */
public class Translations {
	private static final Collection<Translation> translations = new LinkedList<>();
	public static final Translation PREFIX = new Translation("messenger.prefix").add(ComponentType.CHAT, text("<yellow><bold>Unity<!bold> "));
	public static final Translation ROLE_OWNER_NAME = new Translation("roles.owner.name").add(ComponentType.CHAT, text("Owner"));
	public static final Translation ROLE_OWNER_PREFIX = new Translation("roles.owner.prefix").add(ComponentType.CHAT, text("<red>"));
	public static final Translation ROLE_OWNER_PREFIX_PLAYER = new Translation("roles.owner.prefix").add(ComponentType.CHAT, text("<red>%player%"));
	public static final Translation ROLE_ADMIN_NAME = new Translation("roles.admin.name").add(ComponentType.CHAT, text("Admin"));
	public static final Translation ROLE_ADMIN_PREFIX = new Translation("roles.admin.prefix").add(ComponentType.CHAT, text("<aqua>"));
	public static final Translation ROLE_ADMIN_PREFIX_PLAYER = new Translation("roles.admin.prefix").add(ComponentType.CHAT, text("<aqua>%player%"));
	public static final Translation ROLE_MOD_NAME = new Translation("roles.mod.name").add(ComponentType.CHAT, text("Mod"));
	public static final Translation ROLE_MOD_PREFIX = new Translation("roles.mod.prefix").add(ComponentType.CHAT, text("<yellow>"));
	public static final Translation ROLE_MOD_PREFIX_PLAYER = new Translation("roles.mod.prefix").add(ComponentType.CHAT, text("<yellow>%player%"));
	public static final Translation ROLE_MEMBER_NAME = new Translation("roles.member.name").add(ComponentType.CHAT, text("Member"));
	public static final Translation ROLE_MEMBER_PREFIX = new Translation("roles.member.prefix").add(ComponentType.CHAT, text("<white>"));
	public static final Translation ROLE_MEMBER_PREFIX_PLAYER = new Translation("roles.member.prefix").add(ComponentType.CHAT, text("<white>%player%"));
	public static final Translation ROLE_GUEST_NAME = new Translation("roles.guest.name").add(ComponentType.CHAT, text("Guest"));
	public static final Translation ROLE_GUEST_PREFIX = new Translation("roles.guest.prefix").add(ComponentType.CHAT, text("<gray>"));
	public static final Translation ROLE_GUEST_PREFIX_PLAYER = new Translation("roles.guest.prefix").add(ComponentType.CHAT, text("<gray>%player%"));
	public static final Translation ARGUMENT_CANNOT_USE_COMMAND = new Translation("parsers.global.cannot-use-command").add(ComponentType.CHAT, text("You cannot use this command!"));
	public static final Translation ARGUMENT_REQUIRE_FACTION = new Translation("parsers.global.needs-faction").add(ComponentType.CHAT, text("You need a faction to use this command!"));
	public static final Translation ARGUMENT_PLAYER_NOT_IN_SAME_FACTION = new Translation("parsers.member.player-not-in-same-faction").add(ComponentType.CHAT, text("<white>%player% <red>is not a member of your faction"));
	public static final Translation ARGUMENT_PLAYER_IN_SAME_FACTION = new Translation("parsers.member.player-not-in-same-faction").add(ComponentType.CHAT, text("<white>%player% <red>is a member of your faction"));
	public static final Translation ARGUMENT_PLAYER_ALREADY_INVITED = new Translation("parsers.invitable.already-invited").add(ComponentType.CHAT, text("<white>%player% <red>is already invited to the faction"));
	public static final Translation ARGUMENT_PLAYER_NOT_INVITED = new Translation("parsers.invited.not-invited").add(ComponentType.CHAT, text("<white>%player% <red>is not invited to the faction"));
	public static final Translation ARGUMENT_JOIN_FACTION_ALREADY_HAS_FACTION = new Translation("parsers.invited.already-in-faction").add(ComponentType.CHAT, text("<red>You already have a faction."));
	public static final Translation ARGUMENT_JOIN_FACTION_NOT_INVITED = new Translation("parsers.invited.not-invited").add(ComponentType.CHAT, text("<white>%faction% <red>is not public and you're not invited to it."));
	public static final Translation ARGUMENT_JOIN_FACTION_BANNED = new Translation("parsers.invited.banned").add(ComponentType.CHAT, text("<white>%faction% <red>has banned you from joining their faction."));
	public static final Translation ARGUMENT_FACTION = new Translation("parsers.invited.suggestion-faction").add(ComponentType.CHAT, text("<yellow>%faction%"));
	public static final Translation ARGUMENT_UNKNOWN_FACTION = new Translation("parsers.invited.not-invited").add(ComponentType.CHAT, text("<red>Unknown faction <white>%faction%<red>."));
	public static final Translation COMMAND_FACTION_DESCRIPTION = new Translation("commands.faction.description").add(ComponentType.CHAT, text("The root command for factions"));
	public static final Translation COMMAND_FACTION_CREATE_DESCRIPTION = new Translation("commands.faction.create.description").add(ComponentType.CHAT, text("Allows players to create factions"));
	public static final Translation COMMAND_FACTION_CREATE_NAME_DESCRIPTION = new Translation("commands.faction.create.name-description").add(ComponentType.CHAT, text("The name of the faction"));
	public static final Translation COMMAND_FACTION_MEMBERS_DESCRIPTION = new Translation("commands.faction.members.description").add(ComponentType.CHAT, text("Allows player to see members of the faction"));
	public static final Translation COMMAND_FACTION_BAN_DESCRIPTION = new Translation("commands.faction.ban.description").add(ComponentType.CHAT, text("Allows player to ban players from the faction"));
	public static final Translation COMMAND_FACTION_KICK_DESCRIPTION = new Translation("commands.faction.kick.description").add(ComponentType.CHAT, text("Allows player to kick members from the faction"));
	public static final Translation COMMAND_FACTION_INVITE_DESCRIPTION = new Translation("commands.faction.invite.description").add(ComponentType.CHAT, text("Allows player to invite players to the faction"));
	public static final Translation COMMAND_FACTION_DEBUG_DESCRIPTION = new Translation("commands.faction.debug.description").add(ComponentType.CHAT, text("Debug commands of unity"));
	public static final Translation COMMAND_FACTION_DEBUG_GUI_SLOTS_DESCRIPTION = new Translation("commands.faction.debug.slots-description").add(ComponentType.CHAT, text("Shows inventor slots of the game's inventories"));
	public static final Translation COMMAND_FACTION_DEBUG_SIGN_DESCRIPTION = new Translation("commands.faction.debug.sign.description").add(ComponentType.CHAT, text("Opens a sign gui which returns all sign lines when closed"));
	public static final Translation COMMAND_FACTION_DEBUG_FORCE_LOAD = new Translation("commands.faction.debug.force-load.description").add(ComponentType.CHAT, text("Allows force (re)loading of factions and players"));
	public static final Translation COMMAND_NO_FACTION_JOIN_FACTION_DESCRIPTION = new Translation("commands.no-faction.join.description").add(ComponentType.CHAT, text("Allows players to join factions."));

	public static final Translation GUI_BUTTON_COMING_SOON_NAME = new Translation("gui.shared.coming-soon.name").add(ComponentType.CHAT, text("<!italic><red>Coming soon..."));
	public static final Translation GUI_BUTTON_COMING_SOON_LORE = new Translation("gui.shared.coming-soon.lore").add(ComponentType.CHAT, text("<!italic><gray>Suggest ideas what could be used here."));
	public static final Translation MESSAGE_COMING_SOON = new Translation("message.shared.coming-soon").add(ComponentType.CHAT, text("<gray>Suggest ideas for factions using <white>/factions suggest <suggestion>"));

	public static final Translation GUI_FACTION = new Translation("gui.faction.title").add(ComponentType.CHAT, text("<aqua>%player% <dark_gray>(<dark_aqua>%faction%<dark_gray>)"));
	public static final Translation GUI_NO_FACTION = new Translation("gui.no-faction.title").add(ComponentType.CHAT, text("<!italic><dark_aqua>%player%"));
	public static final Translation GUI_FACTION_MEMBERS = new Translation("gui.faction.members.title").add(ComponentType.CHAT, text("<!italic><aqua>Members <dark_gray>(<dark_aqua>%page%<dark_gray>/<dark_aqua>%pages%<dark_gray>)"));
	public static final Translation GUI_FACTION_BANNER_EDITOR = new Translation("gui.factions.members.title").add(ComponentType.CHAT, text("<!italic><yellow>Banner Editor"));
	public static final Translation GUI_FACTION_MEMBER_MANAGE = new Translation("gui.faction.member-manage.title").add(ComponentType.CHAT, text("<!italic><aqua>%player%"));
	public static final Translation GUI_FACTION_KICK = new Translation("gui.faction.kick.title").add(ComponentType.CHAT, text("<!italic><aqua>Confirm <dark_aqua>%player%<aqua>'s kick"));
	public static final Translation GUI_FACTION_BAN = new Translation("gui.faction.ban.title").add(ComponentType.CHAT, text("<!italic><aqua>Confirm <dark_aqua>%player%<aqua>'s ban"));
	public static final Translation GUI_FACTION_INVITE = new Translation("gui.faction.invite.title").add(ComponentType.CHAT, text("<!italic><aqua>Invite <dark_gray>(<dark_aqua>%page%<dark_gray>/<dark_aqua>%pages%<dark_gray>)"));
	public static final Translation GUI_BUTTON_USER_INFO_NAME = new Translation("gui.faction.user.name").add(ComponentType.CHAT, text("<yellow><!italic>%player%"));
	public static final Translation GUI_BUTTON_USER_INFO_FACTION_DESCRIPTION = new Translation("gui.faction.user.description").add(ComponentType.CHAT,
			text("""
					<gray><!italic><yellow>Faction: <white>%faction%
					<gray><!italic><yellow>Member Since: <white>%faction_first_joined%""")
			);
	public static final Translation GUI_BUTTON_USER_INFO_NO_FACTION_DESCRIPTION = new Translation("gui.no-faction.user.description").add(ComponentType.CHAT,
			text("<!italic><gray>You currently do not have a faction."));

	public static final Translation GUI_BUTTON_CREATE_FACTION_NAME = new Translation("gui.no-faction.button.create.name").add(ComponentType.CHAT, text("<!italic><yellow>Create a faction"));
	public static final Translation GUI_BUTTON_CREATE_FACTION_DESCRIPTION = new Translation("gui.no-faction.button.create.description").add(ComponentType.CHAT,
			text("""
					<!italic><gray>Click to create a new faction.<reset>
					<yellow><!italic>Rules:</yellow>
					<gray><!italic> - <yellow>Max length: <white>%configuration_faction_name_max_length%<reset>
					<gray><!italic> - <yellow>Min length: <white>%configuration_faction_name_min_length%<reset>
					<gray><!italic> - <yellow>Characters: <white>A<yellow>→<white>Z<gray>, <white>a<yellow>→<white>z<gray>, <white>0<yellow>→<white>9<gray>, <white>_""")
	);
	public static final Translation GUI_BUTTON_FACTION_MEMBERS_NAME = new Translation("gui.faction.button.members.name").add(ComponentType.CHAT, text("<!italic><yellow>Members"));
	public static final Translation GUI_BUTTON_FACTION_MEMBERS_DESCRIPTION = new Translation("gui.faction.button.members.description").add(ComponentType.CHAT, text("<gray><!italic>Click to see current faction members"));

	public static final Translation GUI_BUTTON_MEMBERS_RETURN_NAME = new Translation("gui.faction.members.button.faction-menu.name").add(ComponentType.CHAT, text("<red><!italic>Return"));
	public static final Translation GUI_BUTTON_MEMBERS_RETURN_DESCRIPTION = new Translation("gui.faction.members.button.faction-menu.description").add(ComponentType.CHAT, text("<gray><!italic>Click to return back to the main faction menu"));
	public static final Translation GUI_BUTTON_MEMBERS_MEMBER_NAME = new Translation("gui.faction.members.button.member.name").add(ComponentType.CHAT, text("<yellow><!italic>%player%"));
	public static final Translation GUI_BUTTON_MEMBERS_MEMBER_DESCRIPTION = new Translation("gui.faction.members.button.member.cannot-manage-description").add(ComponentType.CHAT, text("<yellow><!italic>"));
	public static final Translation GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_NAME = new Translation("gui.faction.members.button.previous-page.name").add(ComponentType.CHAT, text("<yellow><!italic>Previous Page"));
	public static final Translation GUI_BUTTON_MEMBERS_PREVIOUS_PAGE_DESCRIPTION = new Translation("gui.faction.members.button.previous-page.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go previous page"));
	public static final Translation GUI_BUTTON_MEMBERS_NEXT_PAGE_NAME = new Translation("gui.faction.members.button.next-page.name").add(ComponentType.CHAT, text("<yellow>Next Page"));
	public static final Translation GUI_BUTTON_MEMBERS_NEXT_PAGE_DESCRIPTION = new Translation("gui.faction.members.button.next-page.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go next page"));

	public static final Translation GUI_BUTTON_FACTION_NON_MEMBERS_NAME = new Translation("gui.faction.button.invite.name").add(ComponentType.CHAT, text("<!italic><yellow>Non Member Players"));
	public static final Translation GUI_BUTTON_FACTION_NON_MEMBERS_DESCRIPTION = new Translation("gui.faction.button.invite.description").add(ComponentType.CHAT, text("<gray><!italic>Click to manage non member players"));
	public static final Translation GUI_BUTTON_INVITE_RETURN_NAME = new Translation("gui.faction.invite.button.faction-menu.name").add(ComponentType.CHAT, text("<red><!italic>Return"));
	public static final Translation GUI_BUTTON_INVITE_RETURN_DESCRIPTION = new Translation("gui.faction.invite.button.faction-menu.description").add(ComponentType.CHAT, text("<gray><!italic>Click to return back to the main faction menu"));
	public static final Translation GUI_BUTTON_INVITE_PLAYER_NAME = new Translation("gui.faction.invite.button.player.name").add(ComponentType.CHAT, text("<yellow><!italic>%player%"));
	public static final Translation GUI_BUTTON_INVITE_PLAYER_DESCRIPTION = new Translation("gui.faction.invite.button.player.description").add(ComponentType.CHAT, text("<gray><!italic>Click to invite <white>%player%<gray> to the faction"));
	public static final Translation GUI_BUTTON_INVITE_PREVIOUS_PAGE_NAME = new Translation("gui.faction.invite.button.previous-page.name").add(ComponentType.CHAT, text("<yellow><!italic>Previous Page"));
	public static final Translation GUI_BUTTON_INVITE_PREVIOUS_PAGE_DESCRIPTION = new Translation("gui.faction.invite.button.previous-page.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go previous page"));
	public static final Translation GUI_BUTTON_INVITE_NEXT_PAGE_NAME = new Translation("gui.faction.invite.button.next-page.name").add(ComponentType.CHAT, text("<yellow>Next Page"));
	public static final Translation GUI_BUTTON_INVITE_NEXT_PAGE_DESCRIPTION = new Translation("gui.faction.invite.button.next-page.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go next page"));

	public static final Translation GUI_BUTTON_MEMBER_MANAGE_KICK_NAME = new Translation("gui.faction.manage-member.button.kick.name").add(ComponentType.CHAT, text("<red><!italic>Kick"));
	public static final Translation GUI_BUTTON_MEMBER_MANAGE_KICK_DESCRIPTION = new Translation("gui.faction.manage-member.button.kick.description").add(ComponentType.CHAT, text("<gray><!italic>Click to kick <white>%player%<gray> from the faction"));
	public static final Translation GUI_BUTTON_MEMBER_MANAGE_BAN_NAME = new Translation("gui.faction.manage-member.button.ban.name").add(ComponentType.CHAT, text("<red><!italic>Ban"));
	public static final Translation GUI_BUTTON_MEMBER_MANAGE_BAN_DESCRIPTION = new Translation("gui.faction.manage-member.button.ban.description").add(ComponentType.CHAT, text("<gray><!italic>Click to ban <white>%player%<gray> from the faction"));
	public static final Translation GUI_BUTTON_MEMBER_MANAGE_RETURN_NAME = new Translation("gui.faction.manage-member.button.return.name").add(ComponentType.CHAT, text("<yellow><!italic>Return"));
	public static final Translation GUI_BUTTON_MEMBER_MANAGE_RETURN_DESCRIPTION = new Translation("gui.faction.manage-member.button.return.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go back to the members menu"));

	public static final Translation GUI_BUTTON_KICK_RETURN_NAME = new Translation("gui.faction.kick.button.return.name").add(ComponentType.CHAT, text("<yellow><!italic>Return"));
	public static final Translation GUI_BUTTON_KICK_RETURN_LORE = new Translation("gui.faction.kick.button.return.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go back to member management menu"));
	public static final Translation GUI_BUTTON_KICK_CONFIRM_NAME = new Translation("gui.faction.kick.button.confirm.name").add(ComponentType.CHAT, text("<green><bold><!italic>CONFIRM"));
	public static final Translation GUI_BUTTON_KICK_CONFIRM_LORE = new Translation("gui.faction.kick.button.confirm.description").add(ComponentType.CHAT, text("<gray><!italic>Click to confirm kick of <white>%player%<gray>"));
	public static final Translation GUI_BUTTON_KICK_CONFIRM_SIGN_NAME = new Translation("gui.faction.kick.button.confirm-sign.name").add(ComponentType.CHAT, text("<green><bold><!italic>CONFIRM"));
	public static final Translation GUI_BUTTON_KICK_CONFIRM_SIGN_LORE = new Translation("gui.faction.kick.button.confirm-sign.description").add(ComponentType.CHAT, text("""
			<gray><!italic>Click to confirm kick of <white>%player%<gray>
			<gray><!italic>Clicking this confirm button opens,
			<dark_gray><!italic> | <gray>a sign to type a reason to ban the user"""));
	public static final Translation GUI_BUTTON_KICK_CANCEL_NAME = new Translation("gui.faction.kick.button.cancel.name").add(ComponentType.CHAT, text("<red><bold><!italic>CANCEL"));
	public static final Translation GUI_BUTTON_KICK_CANCEL_LORE = new Translation("gui.faction.kick.button.cancel.description").add(ComponentType.CHAT, text("<gray><!italic>Click to cancel kick of <white>%player%<gray>"));

	public static final Translation GUI_BUTTON_BAN_RETURN_NAME = new Translation("gui.faction.ban.button.return.name").add(ComponentType.CHAT, text("<yellow><!italic>Return"));
	public static final Translation GUI_BUTTON_BAN_RETURN_LORE = new Translation("gui.faction.ban.button.return.description").add(ComponentType.CHAT, text("<gray><!italic>Click to go back to member management menu"));
	public static final Translation GUI_BUTTON_BAN_CONFIRM_NAME = new Translation("gui.faction.ban.button.confirm.name").add(ComponentType.CHAT, text("<green><bold><!italic>CONFIRM"));
	public static final Translation GUI_BUTTON_BAN_CONFIRM_LORE = new Translation("gui.faction.ban.button.confirm.description").add(ComponentType.CHAT, text("<gray><!italic>Click to confirm ban of <white>%player%<gray>"));
	public static final Translation GUI_BUTTON_BAN_CONFIRM_SIGN_NAME = new Translation("gui.faction.ban.button.confirm-sign.name").add(ComponentType.CHAT, text("<green><bold><!italic>CONFIRM"));
	public static final Translation GUI_BUTTON_BAN_CONFIRM_SIGN_LORE = new Translation("gui.faction.ban.button.confirm-sign.description").add(ComponentType.CHAT, text("""
			<gray><!italic>Click to confirm ban of <white>%player%<gray>
			<gray><!italic>Clicking this confirm button opens,
			<dark_gray><!italic> | <gray>a sign to type a reason to ban the user"""));
	public static final Translation GUI_BUTTON_BAN_CANCEL_NAME = new Translation("gui.faction.ban.button.cancel.name").add(ComponentType.CHAT, text("<red><bold><!italic>CANCEL"));
	public static final Translation GUI_BUTTON_BAN_CANCEL_LORE = new Translation("gui.faction.ban.button.cancel.description").add(ComponentType.CHAT, text("<gray><!italic>Click to cancel ban of <white>%player%<gray>"));

	public static final Translation SIGN_GUI_TEXT_CREATE = new Translation("sign-gui.create.lines").add(ComponentType.CHAT,
			text("""
					
					^^^^^^^^^^^^^^^^
					Name Of Faction
					 %configuration_faction_name_min_length%-%configuration_faction_name_max_length% chars""")
	);

	public static final Translation MESSAGE_CANNOT_USE_WHILE_IN_FACTION = new Translation("global.error.")

	public static final Translation MESSAGE_FACTION_REQUIRE_NO_FACTION = new Translation("global.error.requires-no-faction").add(ComponentType.CHAT, text("<red>You cannot use this while in a faction!"));
	public static final Translation MESSAGE_FACTION_REQUIRE_FACTION = new Translation("global.error.requires-no-faction").add(ComponentType.CHAT, text("<red>You cannot use this while in you're not in a faction!"));
	public static final Translation MESSAGE_FACTION_CREATE_SIGN_NEED_NAME = new Translation("create.error.sign.require-name").add(ComponentType.CHAT, text("<red>Canceled faction creation"));
	public static final Translation MESSAGE_FACTION_CREATE_TOO_SHORT_NAME = new Translation("create.error.shared.too-short-name").add(ComponentType.CHAT, text("<red>You've entered a too short faction name <gray>(Min length: <white>%configuration_faction_name_min_length%<gray>)"));
	public static final Translation MESSAGE_FACTION_CREATE_TOO_LONG_NAME = new Translation("create.error.shared.too-long-name").add(ComponentType.CHAT, text("<red>You've entered a too long faction name <gray>(Max length: <white>%configuration_faction_name_max_length%<gray>)"));
	public static final Translation MESSAGE_FACTION_CREATE_ALREADY_EXISTS = new Translation("create.error.shared.existing-name").add(ComponentType.CHAT, text("<red>You've entered a faction name which already exists in the server"));
	public static final Translation MESSAGE_FACTION_CREATE_INVALID_NAME = new Translation("create.error.shared.invalid-name").add(ComponentType.CHAT, text("<red>You've entered a faction name with invalid characters <gray>(<white>A<yellow>→<white>Z<gray>, <white>a<yellow>→<white>z<gray>, <white>0<yellow>→<white>9<gray>, <white>_<gray>)"));
	public static final Translation MESSAGE_FACTION_CREATE_BANNED_NAME = new Translation("create.error.shared.banned-name").add(ComponentType.CHAT, text("<red>You've entered a faction name which is not allowed in this server"));
	public static final Translation MESSAGE_FACTION_CREATED = new Translation("create.success.shared.created").add(ComponentType.CHAT, text("<green><bold>SUCCESS<!bold> <white>Successfully created faction named <yellow>%name%"));
	public static final Translation BROADCAST_FACTION_CREATED = new Translation("create.success.shared.created-global").add(ComponentType.CHAT, text("<yellow>%player% <white>has created faction named <yellow>%name%"));

	public static final Translation MESSAGE_FACTION_KICK_NO_PERMISSIONS = new Translation("kick.error.shared.no-permissions").add(ComponentType.CHAT, text("<red>You do not have permissions to kick in this faction"));
	public static final Translation MESSAGE_FACTION_KICK_CANNOT_KICK_USER = new Translation("kick.error.shared.cannot-kick-higher-member").add(ComponentType.CHAT, text("<red>You cannot kick <white>%who%<red> from the faction"));
	public static final Translation MESSAGE_FACTION_KICK_KICKED = new Translation("kick.error.shared.kicked").add(ComponentType.CHAT, text("<red>Kicked <white>%who%<red> from the faction due to <white>%reason%"));
	public static final Translation MESSAGE_FACTION_KICK_WHO_WAS_KICKED = new Translation("kick.error.shared.kicked-kicked-info").add(ComponentType.CHAT, text("<red>You were kicked by <white>%player%<red> from <white>%faction%<red> due to <white>%reason%"));
	public static final Translation BROADCAST_FACTION_KICK_KICKED = new Translation("kick.error.shared.faction-info").add(ComponentType.CHAT, text("<white>%who%<green> was kicked from the faction by <white>%player%<green> due to <white>%reason%"));
	public static final Translation MESSAGE_FACTION_KICK_NO_REASON = new Translation("kick.error.shared.no-reason").add(ComponentType.CHAT, text("<white>no reason listed"));

	public static final Translation MESSAGE_FACTION_BAN_NO_PERMISSIONS = new Translation("kick.error.shared.no-permissions").add(ComponentType.CHAT, text("<red>You do not have permissions to ban in this faction"));
	public static final Translation MESSAGE_FACTION_BAN_CANNOT_BAN_USER = new Translation("kick.error.shared.cannot-ban-higher-member").add(ComponentType.CHAT, text("<red>You cannot ban <white>%who%<red> from the faction"));
	public static final Translation MESSAGE_FACTION_BAN_ALREADY_BANNED = new Translation("kick.error.shared.already-banned").add(ComponentType.CHAT, text("<white>%who% <red>is already banned from the faction for <white>%old_reason%"));
	public static final Translation MESSAGE_FACTION_BAN_BANNED = new Translation("kick.error.shared.banned").add(ComponentType.CHAT, text("<red>Banned <white>%who%<red> from the faction due to <white>%reason%"));
	public static final Translation MESSAGE_FACTION_BAN_WHO_WAS_BANNED = new Translation("ban.error.shared.banned-banned-info").add(ComponentType.CHAT, text("<red>You were kicked by <white>%player%<red> from <white>%faction%<red> due to <white>%reason%"));
	public static final Translation BROADCAST_FACTION_BAN_BANNED = new Translation("ban.error.shared.faction-info").add(ComponentType.CHAT, text("<white>%who%<green> was kicked from the faction by <white>%player%<green> due to <white>%reason%"));
	public static final Translation MESSAGE_FACTION_BAN_NO_REASON = new Translation("ban.error.shared.no-reason").add(ComponentType.CHAT, text("<white>no reason given"));

	public static final Translation MESSAGE_PLAYER_PLAYER_IS_NOT_A_CLAN_MEMBER = new Translation("global.error.player-is-not-clan-member").add(ComponentType.CHAT, text("<white>%who% <red>is not inside your faction."));

	public static final Translation MESSAGE_FACTION_INVITE_NOT_ONLINE = new Translation("invite.error.gui.not-online").add(ComponentType.CHAT, text("<red>You cannot invite <white>%player% <red>because they are not online."));
	public static final Translation MESSAGE_FACTION_INVITE_RECEIVE = new Translation("invite.success.shared.received").add(ComponentType.CHAT, text("<green>You have received an faction invitation from <white>%faction%"));
	public static final Translation MESSAGE_FACTION_INVITE_SENT = new Translation("invite.success.shared.sent").add(ComponentType.CHAT, text("<green>You have sent join invitation to <white>%who%"));
	public static final Translation BROADCAST_FACTION_INVITE_SENT = new Translation("invite.success.shared.sent.broadcast").add(ComponentType.CHAT, text("<white>%player% <green> has sent join invitation to <white>%who%"));
	public static final Translation MESSAGE_FACTION_INVITE_EXPIRE = new Translation("invite.error.shared.expired.player").add(ComponentType.CHAT, text("<red>Your invitation to <white>%faction%<red> has expired"));
	public static final Translation BROADCAST_FACTION_INVITE_EXPIRE = new Translation("invite.error.shared.expired.faction").add(ComponentType.CHAT, text("<red>Invitation to <white>%player%<red> has expired"));

	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_TITLE = new Translation("gui.faction.non-members.manage").add(ComponentType.CHAT, text("<!italic><aqua>%player%"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_RETURN_TITLE = new Translation("gui.faction.non-members.return.title").add(ComponentType.CHAT, text("<!italic><red>Return"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_RETURN_DESCRIPTION = new Translation("gui.faction.non-members.return.description").add(ComponentType.CHAT, text("<gray><!italic>Click to return back to the players menu"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_ONLINE_TITLE = new Translation("gui.faction.non-members.invite-online.title").add(ComponentType.CHAT, text("<!italic><yellow>Invite"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_ONLINE_DESCRIPTION = new Translation("gui.faction.non-members.invite-online.description").add(ComponentType.CHAT, text("<!italic><gray>Click to invite <white>%player% <gray>to the faction"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_OFFLINE_TITLE = new Translation("gui.faction.non-members.invite-offline.title").add(ComponentType.CHAT, text("<!italic><red>Invite"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_OFFLINE_DESCRIPTION = new Translation("gui.faction.non-members.invite-offline.description").add(ComponentType.CHAT, text("<!italic><gray>Cannot invite <white>%player% <gray>as they are not online"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_CANCEL_TITLE = new Translation("gui.faction.non-members.invite-cancel.title").add(ComponentType.CHAT, text("<!italic><yellow>Invite"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_INVITE_CANCEL_DESCRIPTION = new Translation("gui.faction.non-members.invite-cancel.description").add(ComponentType.CHAT, text("<!italic><gray>Click to cancel faction invitation of <white>%player%"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_BAN_TITLE = new Translation("gui.faction.non-members.ban.title").add(ComponentType.CHAT, text("<!italic><red>Invite"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_BAN_DESCRIPTION = new Translation("gui.faction.non-members.ban.description").add(ComponentType.CHAT, text("<!italic><gray>Click to ban <white>%player% <gray>from the faction"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_TITLE = new Translation("gui.faction.non-members.unban.title").add(ComponentType.CHAT, text("<!italic><yellow>Unban"));
	public static final Translation GUI_FACTION_MANAGE_NON_MEMBER_UNBAN_DESCRIPTION = new Translation("gui.faction.non-members.unban.description").add(ComponentType.CHAT, text("<!italic><gray>Click to unban <white>%player%<gray> from the faction"));

	public static final Translation MESSAGE_JOIN_BANNED_FROM_FACTION = new Translation("join.error.banned-from-faction").add(ComponentType.CHAT, text("<red>You've been banned from <white>%faction%<red>."));
	public static final Translation MESSAGE_JOIN_NOT_INVITED = new Translation("join.error.not-invited").add(ComponentType.CHAT, text("<red>You've not been invited to <white>%faction%<red>."));
	public static final Translation MESSAGE_JOIN_JOINED_USING_INVITE = new Translation("join.success.invite.player").add(ComponentType.CHAT, text("<green>Accepted <white>%faction%<green>'s invite."));
	public static final Translation BROADCAST_JOIN_JOINED_USING_INVITE = new Translation("join.success.invite.faction").add(ComponentType.CHAT, text("<white>%player%<green> has accepted the faction invite."));
	public static final Translation MESSAGE_JOIN_JOINED_PUBLIC = new Translation("join.success.public.player").add(ComponentType.CHAT, text("<green>Joined public faction <white>%faction%<green>"));
	public static final Translation BROADCAST_JOIN_JOINED_PUBLIC = new Translation("join.success.public.faction").add(ComponentType.CHAT, text("<white>%player%<green> has joined the faction."));

	/**
	 * Returns all translation keys used by unity.
	 * @return translations
	 */
	public static Collection<Translation> getTranslations(){
		init();
		return translations;
	}

	static void init(){
		Field[] fields = Translations.class.getFields();
		for (Field field : fields){
			try {
				if (!field.canAccess(null)){
					continue;
				}
				if (field.get(null) instanceof Translation translation){
					translations.add(translation);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}


}
