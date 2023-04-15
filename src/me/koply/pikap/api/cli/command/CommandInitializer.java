package me.koply.pikap.api.cli.command;

import org.reflections8.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandInitializer {

    private final Map<String, CommandClassData> commands;
    public CommandInitializer(Map<String, CommandClassData> commands) {
        this.commands = commands;
    }

    public void registerPackage(String...packages) {
        if (packages.length == 0) throw new IllegalArgumentException("Packages cannot be empty.");
        Set<Class<? extends CLICommand>> classes = new HashSet<>();
        for (String cake : packages) {
            classes.addAll(getClasses(cake));
        }
        try {
            registerCommands(classes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerInstance(CLICommand instance) {
        Class<? extends CLICommand> clazz = instance.getClass();

        if (!Modifier.isPublic(clazz.getModifiers())) return;
        Map<String, CommandClassData.MethodAndAnnotation> commandMethodsWithAliases = getCommandMethods(clazz);

        CommandClassData commandClassData = new CommandClassData(clazz, instance, commandMethodsWithAliases);
        registerCommand(commandMethodsWithAliases.keySet(), commandClassData);

    }

    // -----------------------------------------------------------
    // ---------------------- PRIVATE API ------------------------
    // -----------------------------------------------------------

    private Set<Class<? extends CLICommand>> getClasses(String cake) {
        Reflections reflections = new Reflections(cake);
        return reflections.getSubTypesOf(CLICommand.class);
    }

    private void registerCommands(Set<Class<? extends CLICommand>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<? extends CLICommand> clazz : classes) {

            // the class must be public
            // bitwise way to do this: (clazz.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC
            if (!Modifier.isPublic(clazz.getModifiers())) continue;

            OnlyInstance onlyInstance = clazz.getDeclaredAnnotation(OnlyInstance.class);
            if (onlyInstance != null) continue;

            Map<String, CommandClassData.MethodAndAnnotation> commandMethodsWithAliases = getCommandMethods(clazz);

            CLICommand instance = clazz.getDeclaredConstructor().newInstance();
            CommandClassData commandClassData = new CommandClassData(clazz, instance, commandMethodsWithAliases);
            registerCommand(commandMethodsWithAliases.keySet(), commandClassData);
        }
    }

    private void registerCommand(Set<String> commandAliases, CommandClassData clazz) {
        for (String str : commandAliases) {
            commands.put(str, clazz);
        }
    }

    private Map<String, CommandClassData.MethodAndAnnotation> getCommandMethods(Class<? extends CLICommand> clazz) {
        Map<String, CommandClassData.MethodAndAnnotation> commandMethodsWithAliases = new HashMap<>();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (!method.getReturnType().equals(Void.TYPE)) continue;
            Command annotation = method.getDeclaredAnnotation(Command.class);
            if (annotation == null) continue;
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 1 && types[0] == CommandEvent.class) {
                /* void command(CommandEvent e); */
                CommandClassData.MethodAndAnnotation maa = new CommandClassData.MethodAndAnnotation(method, annotation);
                for (String usage : annotation.usages()) {
                    commandMethodsWithAliases.put(usage, maa);
                }
            }
        }
        return commandMethodsWithAliases;
    }
}