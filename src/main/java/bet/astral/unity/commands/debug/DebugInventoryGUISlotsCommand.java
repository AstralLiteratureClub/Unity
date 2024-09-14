package bet.astral.unity.commands.debug;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.bootstrap.UnityCommandBootstrapRegister;
import bet.astral.unity.messenger.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;

import java.util.HashMap;
import java.util.Map;

@Cloud
public class DebugInventoryGUISlotsCommand extends DebugCommand {
	private final Map<InventoryType, InventoryGUI> guis = new HashMap<>();
	public DebugInventoryGUISlotsCommand(UnityCommandBootstrapRegister registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(registerer, commandManager);
	}
	public void registerCommands(){
		command(debugRoot, "slots", Translations.COMMAND_FACTION_DEBUG_GUI_SLOTS_DESCRIPTION, b->
				b.permission("unity.debug.slots")
						.optional(EnumParser.enumComponent(InventoryType.class)
								.name("inventory")
								.defaultValue(DefaultValue.constant(InventoryType.CHEST)))
						.senderType(Player.class)
						.handler(context->{
							Player player = context.sender();
							InventoryType type = context.get("inventory");
							boolean bigChest = false;
							switch (type){
								case CHEST -> bigChest = true;
								case SHULKER_BOX, ENDER_CHEST, BARREL -> type = InventoryType.CHEST;
								case CRAFTER, DROPPER, DISPENSER -> type = InventoryType.DISPENSER;
								case DECORATED_POT, JUKEBOX, COMPOSTER, CREATIVE, MERCHANT -> {
									player.sendRichMessage("<dark_red><bold>You cannot use this inventory using this command!");
									return;
								}
							}
							if (type == InventoryType.PLAYER){
								player.getInventory().clear();
								for (int i = 1; i < player.getInventory().getSize(); i++){
									ItemStack itemStack = ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
									int finalI = i;
									itemStack.editMeta(meta->{
										meta.displayName(Component.text(finalI).decoration(TextDecoration.ITALIC, false));
									});
									player.getInventory().setItem(i, itemStack);
								}
								ItemStack itemStack = ItemStack.of(Material.RED_STAINED_GLASS_PANE);
								itemStack.editMeta(meta->{
									meta.displayName(Component.text(0).decoration(TextDecoration.ITALIC, false));
								});
								player.getInventory().setItem(0, itemStack);
								player.getScheduler().run(unity(), t->player.openInventory(player.getInventory()), null);
								return;
							}
							if (guis.get(type) != null){
								guis.get(type).open(player);
								return;
							}
							Inventory inventory;
							InventoryGUIBuilder builder;
							if (type==InventoryType.CHEST){
								int slots = bigChest ? 6 : 3;
								builder = InventoryGUI.builder(slots);
								inventory = Bukkit.createInventory(null, slots*9);
							} else {
								builder = InventoryGUI.builder(type);
								inventory = Bukkit.createInventory(null, type);
							}

							builder.title(TranslationKey.of("GUI Slots ("+type+")"));
							for (int i = 1; i < inventory.getSize(); i++){
								ItemStack itemStack = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE);
								int finalI = i;
								itemStack.editMeta(meta-> meta.displayName(Component.text(finalI).decoration(TextDecoration.ITALIC, false)));
								itemStack.setAmount(i);
								builder.addClickable(i, new ClickableBuilder(itemStack).actionGeneral(((clickable, itemStack1, player1) -> player1.sendRichMessage("<gray>Clicked on slot: <yellow>"+ finalI))));
							}
							ItemStack itemStack = ItemStack.of(Material.RED_STAINED_GLASS_PANE);
							itemStack.editMeta(meta-> meta.displayName(Component.text(0).decoration(TextDecoration.ITALIC, false)));
							itemStack.setAmount(1);
							builder.addClickable(0, new ClickableBuilder(itemStack).actionGeneral(((clickable, itemStack1, player1) -> player1.sendRichMessage("<gray>Clicked on slot: <yellow>"+ 0))));

							InventoryGUI gui = builder.build();
							guis.put(type, gui);

							gui.open(player);
						})
		).register();
	}
}
