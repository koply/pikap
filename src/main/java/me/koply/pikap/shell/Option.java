package me.koply.pikap.shell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Option {

    // Example: reverse (--reverse)
    String name();

    // Example r (-r)
    String shortName();

    // Example: Reverses the query...
    String description();

    // If true, the option is required
    boolean required() default false;

    // Example: Integer.class, String.class, etc...
    Class<?> acceptedType() default String.class;
}