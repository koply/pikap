package me.koply.pikap.shell;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.shell.commands.HelpCommand;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Slf4j
public class CommandRegistry {

    private final Map<String, MethodInvoker> commandMap = new HashMap<>();

    @Inject
    public CommandRegistry(Set<Object> commandHandlers) {
        register(commandHandlers);

        register(new HelpCommand(commandMap));
    }

    private void register(@NotNull Set<Object> commandHandlers) {
        for (Object handler : commandHandlers) {
            register(handler);
        }
    }

    public void register(@NotNull Object handler) {
        for (Method method : handler.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            if (!(method.getReturnType() == void.class || method.getReturnType() == boolean.class)) {
                log.info("Method {} has return type {}, expected void or boolean.", method.getName(), method.getReturnType().getName());
                continue;
            }

            int parameterCount = method.getParameterCount();
            if (parameterCount != 1) {
                log.info("Method {} has {} parameters, expected 1.", method.getName(), parameterCount);
                continue;
            }

            Class<?> parameterType = method.getParameterTypes()[0];
            if (!CommandEvent.class.isAssignableFrom(parameterType)) {
                log.info("Method {} has parameter of type {}, expected {}", method.getName(), parameterType.getName(), CommandEvent.class.getName());
                continue;
            }

            Command commandAnnotation = method.getAnnotation(Command.class);
            boolean returnsBool = method.getReturnType() == boolean.class;
            var invoker = new MethodInvoker(handler, method, returnsBool, commandAnnotation);

            for (String usage : commandAnnotation.usages()) {
                commandMap.put(usage, invoker);
            }
        }
    }

}