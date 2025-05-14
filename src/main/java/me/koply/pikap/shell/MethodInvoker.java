package me.koply.pikap.shell;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

import static me.koply.pikap.util.ColorHelper.*;

@Slf4j
public class MethodInvoker {

    private final Method method;
    private final boolean returnsBool;

    @Getter
    private final Object target;

    @Getter
    private final Command commandAnnotation;

    MethodInvoker(Object target, Method method, boolean returnsBool, Command commandAnnotation) {
        this.target = target;
        this.method = method;
        this.returnsBool = returnsBool;
        this.commandAnnotation = commandAnnotation;
    }

    public void invoke(CommandEvent event) {
        try {
            Object result = method.invoke(target, event);
            if (returnsBool && !((boolean) result)) {
                System.out.println(getUsage());
            }
        } catch (Exception e) {
            log.error("Failed to invoke method {}", method, e);
        }
    }

    public String getUsage() {
        var sb = new StringBuilder();

        sb.append(BLUE);
        sb.append(String.join(", ", commandAnnotation.usages()));
        sb.append(": ");
        sb.append(RESET);
        sb.append(yellow(commandAnnotation.description()));
        sb.append("\n");

        for (Option option : commandAnnotation.options()) {
            sb.append(CYAN);
            sb.append("\t-");
            sb.append(option.shortName());
            sb.append(", --");
            sb.append(option.name());
            sb.append(": ");
            sb.append(RESET);
            sb.append(YELLOW);
            sb.append(option.description());
            if (option.required()) sb.append(" (").append(option.acceptedType().getSimpleName()).append(" value required)");
            sb.append(RESET);
            sb.append("\n");
        }

        return sb.toString();
    }
}