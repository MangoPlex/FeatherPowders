package featherpowders.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import featherpowders.items.ItemsDriver;

/**
 * Annotation Commands API: Base class. Extends this class and starts using annotations on top of
 * every method names to create new subcommand. The annotation commands system also handles tab
 * completion. To register your command, simply register it like how you've done with old fashioned
 * way (something like <code>getCommand("42").setExecutor(new CustomCommand())</code>)
 * @author nahkd
 *
 */
public abstract class Command implements CommandExecutor, TabCompleter {
    
    private ArrayList<MatchingEntry> _entries = new ArrayList<>();
    
    public Command() {
        Class<? extends Command> clazz = this.getClass();
        try {
            for (Method method : clazz.getDeclaredMethods()) {
                ArgumentsMatch argsMatch = method.getDeclaredAnnotation(ArgumentsMatch.class);
                if (argsMatch == null) continue;
                
                MatchingEntry entry = new MatchingEntry(this, argsMatch, method);
                _entries.add(entry);
            }
            
            Collections.sort(_entries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private MatchingEntry findFromArgs(List<String> methodArgs, String... args) {
        for (MatchingEntry entry : _entries) {
            if (entry.match(methodArgs, args)) return entry;
            if (methodArgs.size() > 0) methodArgs.clear();
        }
        return null;
    }
    
    /**
     * Send error message with suggestion
     * @param sender
     * @param message
     * @param suggestion
     */
    public void sendError(CommandSender sender, String message, String suggestion) {
        sender.sendMessage(new String[] {
            "§cError while executing command: §f" + message,
            "§7" + suggestion
        });
    }
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> methodArgs = new ArrayList<>();
        MatchingEntry entry = findFromArgs(methodArgs, args);
        if (entry == null) {
            sendError(sender, "No command found", "Please check your input");
            return true;
        }
        
        Object[] methodInputArgs = new Object[1 + methodArgs.size()];
        methodInputArgs[0] = sender;
        
        String[] convertedMethodArgs = methodArgs.toArray(String[]::new);
        System.arraycopy(convertedMethodArgs, 0, methodInputArgs, 1, convertedMethodArgs.length);
        
        try {
            entry.method.invoke(this, methodInputArgs);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(sender, "An error occured", "Please check your console for information");
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String[] matchArgs = new String[args.length - 1];
        System.arraycopy(args, 0, matchArgs, 0, args.length - 1);
        String keyword = args[args.length - 1];
        
        ArrayList<String> suggestions = new ArrayList<>();
        for (MatchingEntry entry : _entries) entry.suggestTab(suggestions, sender, keyword, matchArgs);
        
        return suggestions;
    }
    
    // Pre-made suggestion methods
    public List<String> _suggestion_selector(CommandSender sender, String keyword) {
        List<String> out = new ArrayList<String>();
        out.addAll(Arrays.asList("@a", "@e", "@s", "@r"));
        if (!keyword.startsWith("@")) out.addAll(Bukkit.getOnlinePlayers().stream().map(v -> v.getName()).toList());
        return out;
    }
    
    public List<String> _suggestion_item_id(CommandSender sender, String keyword) {
        return ItemsDriver.getDefaultDriver().getAllTypes().stream().map(v -> v.dataId).toList();
    }
    
}
