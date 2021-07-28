package featherpowders.commands;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation Commands API: Arguments matching annotation. This annotation is available in runtime, which allow FeatherPowders to
 * create command
 * @author nahkd
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ArgumentsMatch {

    /**
     * Arguments pattern.
     * @return
     */
    Pattern[] pattern();
    
}
