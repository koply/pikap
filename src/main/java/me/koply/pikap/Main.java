package me.koply.pikap;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.app.PikapApplication;
import me.koply.pikap.di.DaggerPikapComponent;
import me.koply.pikap.di.PikapComponent;
import me.koply.pikap.shell.CommandHandler;

@Slf4j
public class Main {

    public static void main(String[] args) {
        PikapComponent component = DaggerPikapComponent.create();
        PikapApplication app = component.getPikapApplication();
        app.init();

        log.info("Starting command dispatcher...");
        CommandHandler commandHandler = component.getCommandHandler();

        try {
            commandHandler.start();
        } catch (Exception e) {
            log.error("Failed to start command dispatcher.", e);
        }
    }

}