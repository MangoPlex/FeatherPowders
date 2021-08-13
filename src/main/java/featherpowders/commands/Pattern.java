package featherpowders.commands;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Pattern {
    
    String[] literal() default {};
    String[] contains() default {};
    boolean readToEnd() default false;
    
    String[] suggest() default {};
    String suggestMethod() default "";
    boolean asMethodArg() default false;
    
}
