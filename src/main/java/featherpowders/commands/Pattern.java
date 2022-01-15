package featherpowders.commands;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Annotation Commands API: Matching pattern.
 * @author nahkd
 *
 */
@Retention(RUNTIME)
public @interface Pattern {
    
    /**
     * An array of literals to match, For subcommands, it's an array of aliases
     * @return
     */
    String[] literal() default {};
    
    /**
     * 
     * @return
     */
    String[] contains() default {};
    
    /**
     * Read command argument to end
     * @return
     */
    boolean readToEnd() default false;
    
    /**
     * An array of suggestions. Use {@link #suggestMethod()} for advanced suggestion
     * @return
     */
    String[] suggest() default {};
    
    /**
     * Call method to get all suggestions. If the prefix is "#", it will uses pre-made suggestions
     * method (which you can find it inside {@link Command}/_suggestion_...()) 
     * @return
     */
    String suggestMethod() default "";
    
    /**
     * Call your subcommand method with argument that's matched from pattern
     * @return
     */
    boolean asMethodArg() default false;
    
}
