package featherpowders.commands;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

public class MatchingEntry implements Comparable<MatchingEntry> {
    
    public final ArgumentsMatch match;
    public final Method method;
    
    public MatchingEntry(ArgumentsMatch match, Method method) {
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
    
    public void suggestTab(List<String> suggestions, String keyword, String... args) {
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
