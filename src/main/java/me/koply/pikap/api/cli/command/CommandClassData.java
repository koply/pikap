package me.koply.pikap.api.cli.command;

import java.lang.reflect.Method;
import java.util.Map;

public class CommandClassData {

    public CommandClassData(Class<? extends CLICommand> clazz, CLICommand instance, Map<String, MethodAndAnnotation> methods) {
        this.clazz = clazz;
        this.instance = instance;
        this.methods = methods;
    }

    public final Class<? extends CLICommand> clazz;
    public final CLICommand instance;
    public final Map<String, MethodAndAnnotation> methods;

    // struct :(
    public static class MethodAndAnnotation {
        public final Method method;
        public final Command annotation;

        public MethodAndAnnotation(Method method, Command annotation) {
            this.method = method;
            this.annotation = annotation;
        }
    }
}