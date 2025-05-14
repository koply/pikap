package me.koply.pikap.shell.commands;

import me.koply.pikap.shell.*;

import java.util.*;

import static me.koply.pikap.util.ColorHelper.*;

public class HelpCommand implements CompletionProvider {

    private final Map<String, MethodInvoker> commands;
    private final String[] commandCategories;

    public HelpCommand(Map<String, MethodInvoker> commands) {
        this.commands = commands;
        commandCategories = generateCommandCategories();
    }

    @Command(usages = {"help", "guide", "commands"},
            description = "This command.",
            category = "Information")
    public void help(CommandEvent event) {
        if (event.args().length == 0) {
            System.out.println(red("Command categories: ") + blue(String.join(", ", commandCategories)));
            return;
        }

        String enteredCategory = event.args()[0].toLowerCase(Locale.ROOT);

        Set<MethodInvoker> duplicationChecker = new HashSet<>();
        if (enteredCategory.equalsIgnoreCase("all")) {
            int commandCounter = 0, optionsCounter = 0;
            for (Map.Entry<String, MethodInvoker> entry : commands.entrySet()) {
                if (duplicationChecker.contains(entry.getValue())) continue;

                System.out.println(entry.getValue().getUsage());
                commandCounter += entry.getValue().getCommandAnnotation().usages().length;
                optionsCounter += entry.getValue().getCommandAnnotation().options().length;
                duplicationChecker.add(entry.getValue());
            }
            System.out.println(green("[ ") + red(commandCounter+"") + green(" commands, ") + red(optionsCounter+"") + green(" options found. ]"));
            return;
        }


        for (Map.Entry<String, MethodInvoker> entry : commands.entrySet()) {
            if (duplicationChecker.contains(entry.getValue())) continue;

            String category = entry.getValue().getCommandAnnotation().category().toLowerCase(Locale.ROOT);
            if (category.startsWith(enteredCategory)) {
                System.out.println(entry.getValue().getUsage());
                duplicationChecker.add(entry.getValue());
            }
        }
    }

    private String[] generateCommandCategories() {
        Set<String> categories = new HashSet<>();

        for (Map.Entry<String, MethodInvoker> entry : commands.entrySet()) {
            String category = entry.getValue().getCommandAnnotation().category();
            if (category.isEmpty() || category.isBlank()) continue;
            categories.add(category.toLowerCase(Locale.ROOT));
        }

        categories.add("all");

        return categories.toArray(new String[0]);
    }


    @Override
    public Map<String, String[]> getCompletions() {
        return Map.of("help", commandCategories);
    }
}