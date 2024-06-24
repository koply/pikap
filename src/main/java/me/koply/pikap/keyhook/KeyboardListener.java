package me.koply.pikap.keyhook;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.sound.SoundManager;

public class KeyboardListener implements NativeKeyListener {

    private final SoundManager soundManager = SoundManager.getInstance();

    public void registerHook() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException ex) {
            Console.info("There was a problem while registering the native keyboard hook.");
        }
    }

    public void unregisterHook() {
        try {
            GlobalScreen.unregisterNativeHook();
            GlobalScreen.removeNativeKeyListener(this);
        } catch (NativeHookException e) {
            Console.info("There was a problem while unregistering the native keyboard hook.");
        }
    }

    // keycode(win-linux) / rawcode(win-linux)
    // previous 57360-0   / unknown-65302
    // next 57369-0       / unknown-65303
    // play/pause 57378-0 / unknown-65300
    // stop unknown       / unknown-65301

    private static final int PLAY_PAUSE_RAW = 65300;
    private static final int STOP_RAW = 65301;
    private static final int PREVIOUS_RAW = 65302;
    private static final int NEXT_RAW = 65303;

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        switch (e.getRawCode()) {
            case PLAY_PAUSE_RAW: // play
                if (soundManager.isPaused()) {
                    soundManager.resume();
                } else {
                    soundManager.pause();
                }
                break;
            case PREVIOUS_RAW:
                // previous
                // TODO
                break;
            case NEXT_RAW:
                soundManager.nextTrack(1);
                break;
            default:
                break;
        }
    }

}