package me.koply.pikap.config;

import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Configuration {

    @ConfigProperty(description = "Performs detailed logging", defaultValue = "false", type = Boolean.class)
    private boolean debug;

    @ConfigProperty(description = "Default audio provider (youtube, yandex, soundcloud, bandcamp, vimeo, twitchstream)", defaultValue = "soundcloud", type = String.class)
    private String defaultAudioProvider;

    @ConfigProperty(description = "Search result list count limitor. -1 is unlimited", defaultValue = "-1", type = Integer.class)
    private int searchLimit;

    @ConfigProperty(description = "Maximum volume, sound distortion may occur when the volume is above 100", defaultValue = "100", type = Integer.class)
    private int maximumVolume;

    @ConfigProperty(description = "Database backend (sqlite)", defaultValue = "sqlite", type = String.class)
    private String db;

    @ConfigProperty(description = "Default data.db", defaultValue = "data.db", type = String.class)
    private String dbFile;

    @ConfigProperty(description = "Record tracks", defaultValue = "false", type = Boolean.class)
    private boolean recorder;

    @ConfigProperty(description = "Recorded sounds folder", defaultValue = "./recs/", type = String.class)
    private String recordingsFolder;

    @ConfigProperty(description = "Item count per pages in queue command", defaultValue = "8", type = Integer.class)
    private int queueListItemCount;

}