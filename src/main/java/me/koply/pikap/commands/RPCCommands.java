package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import de.jcm.discordgamesdk.Core;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.discord.DiscordRPC;
import me.koply.pikap.util.StringUtil;

public class RPCCommands implements CLICommand {

    @Command(usages = "rpc", desc = "DiscordRPC settings and monitoring.")
    public void rpc(CommandEvent e) {
        Core core = DiscordRPC.getInstance().getCore();
        boolean isOpen = core != null && core.isOpen();

        if (e.getArgs().length != 2) { // status
            println("DiscordRPC status: " + (isOpen ? Chalk.on("Active").green() : Chalk.on("Inactive").red()));
            if (isOpen) {
                String user = Chalk.on("User Not Found").red().toString();
                try {
                    user = "User: " + Chalk.on(core.userManager().getCurrentUser().getUsername()).green();
                } catch (Exception ignored) {}
                println(user);
            }
            return;
        }

        if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "on", "open", "activate")) {
            if (DiscordRPC.getInstance().prepare()) {
                DiscordRPC.getInstance().loadAsync();
            }
        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "off", "close", "deactivate")) {
            DiscordRPC.getInstance().close();
        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "res", "reset", "load")) {
            DiscordRPC.getInstance().close();
            if (DiscordRPC.getInstance().prepare()) {
                DiscordRPC.getInstance().loadAsync();
            }
        }
    }

}
