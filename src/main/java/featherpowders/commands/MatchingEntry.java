package featherpowders.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

public class MatchingEntry implements Comparable<MatchingEntry> {
    
    public final Command command;
    public final ArgumentsMatch match;
    public final Method method;
    
    public MatchingEntry(Command command, ArgumentsMatch match, Method method) {
        this.command = command;
        this.match = match;
        this.method = method;
    }
    
    public boolean match(List<String> methodArgs, String... args) {
        if (
            (match.pattern().length == 0 && args.length > 0) ||
            (match.pattern().length > 0 && args.length == 0)
        ) return false;
        
        int argPos = 0;
        String arg;
        
        for (int i = 0; i < match.pattern().length; i++) {
            if (args.length <= argPos) return false;
            Pattern pat = match.pattern()[i];
            arg = args[argPos++];
            
            boolean endHere = false;
            boolean literalMatched = false;
            
            if (pat.literal().length > 0) for (String literal : pat.literal()) if (arg.equalsIgnoreCase(literal)) {
                literalMatched = true;
                break;
            }
            else if (pat.contains().length > 0) for (String contains : pat.contains()) if (!arg.contains(contains)) return false;
            else if (pat.readToEnd()) {
                if (i != match.pattern().length - 1) throw new RuntimeException("readToEnd pattern placed at wrong location!");
                while (argPos < args.length) arg += " " + args[argPos++];
                endHere = true;
            }
            
            if (pat.asMethodArg()) methodArgs.add(arg);
            if (endHere) return true;
            
            if (pat.literal().length > 0 && !literalMatched) return false;
        }
        return true;
    }
    
    public void suggestTab(List<String> suggestions, CommandSender sender, String keyword, String... args) {
        int argPos = 0;
        String arg;
        
        for (int i = 0; i < match.pattern().length; i++) {
            Pattern pat = match.pattern()[i];
            if (args.length <= argPos) {
                if (pat.literal().length > 0) suggestions.addAll(Stream.of(pat.literal()).filter(p -> p.startsWith(keyword)).toList());
                else if (pat.contains().length > 0) for (String contains : pat.contains()) {
                    suggestions.add(keyword + contains);
                    suggestions.add(contains + keyword);
                }
                suggestions.addAll(Stream.of(pat.suggest()).filter(p -> p.startsWith(keyword)).toList());
                
                String methodName = pat.suggestMethod();
                if (methodName.length() > 0) try {
                    if (methodName.startsWith("#")) methodName = "_suggestion_" + methodName.substring(1).replaceAll("-", "_");
                    
                    Method method = command.getClass().getMethod(methodName, CommandSender.class, String.class);
                    Object obj = method.invoke(command, sender, keyword);
                    if (obj instanceof List<?> list) {
                        suggestions.addAll(list.stream().map(v -> v.toString()).filter(v -> v.startsWith(keyword)).toList());
                    } else {
                        String[] arr = (String[]) obj;
                        suggestions.addAll(Stream.of(arr).map(v -> v.toString()).filter(v -> v.startsWith(keyword)).toList());
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    System.err.println("[FeatherPowders/Commands] No such suggestion method: " + methodName + "(CommandSender, String)");
                    System.err.println("Please add this method inside your command class");
                    System.err.println(" - #1: CommandSender that request tab complete");
                    System.err.println(" - #2: Input keyword");
                    System.err.println(" - Return: String[] | List<Object>");
                } catch (IllegalAccessException e) {
                    System.err.println("[FeatherPowders/Commands] Cannot access method: " + method.toString() + ". Please check your method modifier");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    System.err.println("[FeatherPowders/Commands] An unexpected exception thrown. Please send the stack trace above to our GitHub issues tab");
                } catch (InvocationTargetException e) {
                    System.err.println("[FeatherPowders/Commands] An exception thrown inside your method: " + method.toString() + ":");
                    e.getTargetException().printStackTrace();
                }
                return;
            }
            arg = args[argPos++];
            
            boolean endHere = false;
            boolean literalMatched = false;
            
            if (pat.literal().length > 0) for (String literal : pat.literal()) if (arg.equalsIgnoreCase(literal)) {
                literalMatched = true;
                break;
            }
            else if (pat.contains().length > 0) for (String contains : pat.contains()) if (!arg.contains(contains)) {
                suggestions.add(keyword + contains);
                suggestions.add(contains + keyword);
                suggestions.addAll(Stream.of(pat.suggest()).filter(p -> p.startsWith(keyword)).toList());
                return;
            } else if (pat.readToEnd()) {
                if (i != match.pattern().length - 1) throw new RuntimeException("readToEnd pattern placed at wrong location!");
                while (argPos < args.length) arg += " " + args[argPos++];
                endHere = true;
            }
            
            if (endHere) return;
            if (pat.literal().length > 0 && !literalMatched) return;
        }
    }
    
    @Override
    public int compareTo(MatchingEntry o) { return o.match.pattern().length - match.pattern().length; }
    
}
