package me.koply.pikap.commands;

import me.koply.pikap.Main;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.sound.recorder.ReadableTrackFile;

import java.util.List;

public class RecordedTrackCommands implements CLICommand {

    final List<ReadableTrackFile> trackFiles = Main.RECORD_MANAGER.getTrackFiles();

    @Command(usages = "list", desc = "Lists recorded tracks.")
    public void listTracks(CommandEvent e) {
        for (ReadableTrackFile file : trackFiles) {

        }
    }

}