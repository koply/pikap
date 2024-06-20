package me.koply.pikap.commands;

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

        if (e.getArgs().length != 2) { // status
            println("DiscordRPC status: " + core.isOpen());
            return;
        }

        if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "on", "open", "activate")) {

        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "off", "close", "deactivate")) {

        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "res", "reset", "load")) {

        }
    }

}
