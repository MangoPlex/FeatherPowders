package featherpowders.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class Command implements CommandExecutor, TabCompleter {
    
    private ArrayList<MatchingEntry> _entries = new ArrayList<>();
    
    public Command() {
        Class<? extends Command> clazz = this.getClass();
        try {
            for (Method method : clazz.getDeclaredMethods()) {
                ArgumentsMatch argsMatch = method.getDeclaredAnnotation(ArgumentsMatch.class);
                if (argsMatch == null) continue;
                
                MatchingEntry entry = new MatchingEntry(argsMatch, method);
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
    
    public void sendError(CommandSender sender, String message, String suggestion) {
        sender.sendMessage(new String[] {
            "§cError while executing command: §f" + message,
            "§7" + suggestion
        });
    }
    
    @Override
    public boolean onCommand(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {
        List<String> methodArgs = new ArrayList<>();
        MatchingEntry entry = findFromArgs(methodArgs, arg3);
        if (entry == null) {
            sendError(arg0, "No command found", "Please check your input");
            return true;
        }
        
        Object[] methodInputArgs = new Object[1 + methodArgs.size()];
        methodInputArgs[0] = arg0;
        
        String[] convertedMethodArgs = methodArgs.toArray(String[]::new);
        System.arraycopy(convertedMethodArgs, 0, methodInputArgs, 1, convertedMethodArgs.length);
        
        try {
            entry.method.invoke(this, methodInputArgs);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(arg0, "An error occured", "Please check your console for information");
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] args) {
        String[] matchArgs = new String[args.length - 1];
        System.arraycopy(args, 0, matchArgs, 0, args.length - 1);
        String keyword = args[args.length - 1];
        
        ArrayList<String> suggestions = new ArrayList<>();
        for (MatchingEntry entry : _entries) entry.suggestTab(suggestions, keyword, matchArgs);
        
        return suggestions;
    }
    
}
