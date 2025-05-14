package me.koply.pikap.shell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String[] usages();
    Option[] options() default {};
    String description() default "";
    String category() default "";
    String notes() default "";
}