package featherpowders.implementations.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import featherpowders.FeatherPowders;
import featherpowders.commands.ArgumentsMatch;
import featherpowders.commands.Command;
import featherpowders.commands.Pattern;
import featherpowders.data.DataDriver;
import featherpowders.items.CustomStack;
import featherpowders.items.CustomType;
import featherpowders.items.ItemsDriver;

public class AdminCommand extends Command {

    private FeatherPowders plugin;
    
    public AdminCommand(FeatherPowders plugin) { this.plugin = plugin; }
    
    //
    // Main
    // /fp
    //
    
    @ArgumentsMatch(pattern = {})
    public void index(CommandSender sender) {
        sender.sendMessage(new String[] {
            "§eFeather§6Powders",
            "§f/fp §7Show this message",
            "§f/fp drivers §7FeatherPowders drivers management",
            "§f/fp items §7Custom items management"
        });
    }
    
    //
    // Drivers
    // /fp drivers
    //
    
    @ArgumentsMatch(pattern = { @Pattern(literal = { "drivers", "driver" }) })
    public void driversIndex(CommandSender sender) {
        sender.sendMessage(new String[] {
            "§eFeather§6Powders§7/§fDrivers Management",
            "§fdrivers list §b[type = all] [search] §7Show all installed drivers"
        });
    }
    
    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "drivers", "driver" }),
        @Pattern(literal = "list")
    })
    public void driversList(CommandSender sender) { driversList(sender, "all"); }

    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "drivers", "driver" }),
        @Pattern(literal = "list"),
        @Pattern(asMethodArg = true, suggest = { "all", "data" })
    })
    public void driversList(CommandSender sender, String type) { driversList(sender, type, ""); }
    
    @SuppressWarnings("unchecked")
    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "drivers", "driver" }),
        @Pattern(literal = "list"),
        @Pattern(asMethodArg = true, suggest = { "all", "data" }),
        @Pattern(asMethodArg = true, readToEnd = true)
    })
    public void driversList(CommandSender sender, String type, String search) {
        sender.sendMessage("§8 --- ");
        sender.sendMessage("§7Listing all drivers with type '" + type + "':");
        
        // TODO: Clean this mess
        
        if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("data")) {
            sender.sendMessage("§7 - §bData Storage Drivers ('data')");
            for (DataDriver driver : DataDriver.getAllDrivers()) if (driver.getDriverName().contains(search)) {
                String line;
                if (DataDriver.getDefaultDriver() == driver) line = "§7    + §b" + driver.getDriverName() + " §f(default)";
                else line = "§7    + §f" + driver.getDriverName();
                sender.sendMessage(line);
            }
        }
        
        if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("item")) {
            sender.sendMessage("§7 - §bCustom Items Drivers ('item')");
            for (ItemsDriver<?, ?> driver : ItemsDriver.getDrivers()) if (((ItemsDriver<CustomType, CustomStack>) driver).typeType.getName().contains(search)) {
                String line;
                if (ItemsDriver.getDefaultDriver() == driver) line = "§7    + §b" + driver.typeType.getName() + " §f(default)";
                else line = "§7    + §f" + driver.typeType.getName();
                sender.sendMessage(line);
            }
        }
        
        sender.sendMessage("§8 --- ");
    }
    
    //
    // Items
    // /fp items
    //
    
    @ArgumentsMatch(pattern = { @Pattern(literal = { "item", "items" }) })
    public void itemsIndex(CommandSender sender) {
        sender.sendMessage(new String[] {
            "§eFeather§6Powders§7/§fCustom Items Management",
            "§fitems give §e<Name> <ID> §b[amount = 1] §7Give item to player",
            "§fitems list §b[search] §7List/search all items"
        });
    }
    
    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "item", "items" }),
        @Pattern(literal = "give"),
        @Pattern(asMethodArg = true),
        @Pattern(asMethodArg = true)
    })
    public void itemsGive(CommandSender sender, String name, String id) { itemsGive(sender, name, id, "1"); }

    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "item", "items" }),
        @Pattern(literal = "give"),
        @Pattern(asMethodArg = true),
        @Pattern(asMethodArg = true),
        @Pattern(asMethodArg = true)
    })
    public void itemsGive(CommandSender sender, String name, String id, String amountText) {
        int amount;
        try {
            amount = Integer.parseInt(amountText);
        } catch (NumberFormatException e) {
            sendError(sender, "Invalid input", "The argument only accept valid integers"); return;
        }
        
        Player player = Bukkit.getPlayer(name);
        if (player == null) { sendError(sender, "Player not found", "Make sure they're online"); return; }
        
        ItemsDriver<CustomType, CustomStack> driver = ItemsDriver.getDefaultDriver();
        if (driver == null) { sendError(sender, "No custom items driver found", "Please install one. I suggest ManyItems"); return; }
        
        CustomType type = driver.fromDataID(id);
        if (type == null) { sendError(sender, "Item ID not found", "Please check again, or use /fp items list"); return; }
        
        CustomStack stack = driver.createItem(type, amount);
        player.getInventory().addItem(driver.fromCustom(stack));
        sender.sendMessage("§7Item " + id + " added to " + name + "'s inventory!");
    }

    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "item", "items" }),
        @Pattern(literal = "list")
    })
    public void itemsList(CommandSender sender) { itemsList(sender, ""); }
    
    @ArgumentsMatch(pattern = {
        @Pattern(literal = { "item", "items" }),
        @Pattern(literal = "list"),
        @Pattern(asMethodArg = true)
    })
    public void itemsList(CommandSender sender, String search) {
        ItemsDriver<CustomType, CustomStack> driver = ItemsDriver.getDefaultDriver();
        if (driver == null) { sendError(sender, "No custom items driver found", "Please install one. I suggest ManyItems"); return; }
        
        Collection<CustomType> types = driver.getAllTypes();
        if (types.size() == 0) { sender.sendMessage("§7uhhh, no items :shurg:"); return; }

        sender.sendMessage("§8 --- ");
        if (search.length() > 0) sender.sendMessage("§7Listing items: §7(Search keyword: \"" + search + "\")");
        else sender.sendMessage("§7Listing items:");
        
        types.stream().filter(p -> p.dataId.contains(search)).forEach(type -> {
            sender.sendMessage("§7 - §f" + type.dataId);
        });
        
        sender.sendMessage("§8 --- ");
    }
    
}