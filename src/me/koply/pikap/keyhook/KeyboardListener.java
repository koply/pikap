package me.koply.pikap.keyhook;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyboardListener implements NativeKeyListener {

    public void registerHook() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException ex) {
            Console.info("There was a problem registering the native keyboard hook.");
        }
    }

    public void unregisterHook() {
        try {
            GlobalScreen.unregisterNativeHook();
            GlobalScreen.removeNativeKeyListener(this);
        } catch (NativeHookException e) {
            Console.info("There was a problem unregistering the native keyboard hook.");
        }
    }

    Logger log = LoggerFactory.getLogger("KeyboardListener");

    // previous keyCode 57360
    // next keyCode 57369
    // play/pause keyCode 57378
    // TODO LINUX KEYBINDINGS
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        log.info("KeyPressed: " + e.getKeyCode());
        switch (e.getKeyCode()) {
            case 57378:
                Main.soundManager.playPauseButton();
                break;
            case 57369:
                Main.soundManager.nextTrack(1);
                break;
            case 57360:
                // previous
                // TODO
                break;
            default:
                break;
        }
    }

}