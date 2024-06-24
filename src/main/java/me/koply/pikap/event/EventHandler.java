package me.koply.pikap.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    // Higher prio runs first
    int priority() default 0;
}
