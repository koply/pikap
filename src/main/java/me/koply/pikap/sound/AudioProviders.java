package me.koply.pikap.sound;

import lombok.Getter;

@Getter
public enum AudioProviders {
    YOUTUBE("ytsearch:", "Youtube"),
    SOUNDCLOUD("scsearch:", "Soundcloud"),
    BANDCAMP("bcsearch:", "Bandcamp"),
    VIMEO("vmsearch:", "Vimeo"),
    TWITCHSTREAM("tssearch:", "Twitch");

    private final String searchPrefix;
    private final String providerName;

    AudioProviders(String searchPrefix, String providerName) {
        this.searchPrefix = searchPrefix;
        this.providerName = providerName;
    }

    public static AudioProviders fromProviderName(String providerName) {
        for (AudioProviders provider : AudioProviders.values()) {
            if (provider.getProviderName().equalsIgnoreCase(providerName)) {
                return provider;
            }
        }
        return null;
    }
}