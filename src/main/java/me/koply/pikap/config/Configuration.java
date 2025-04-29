package me.koply.pikap.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    @ConfigProperty(description = "Performs detailed logging", defaultValue = "false")
    private boolean debug;

    @ConfigProperty(description = "Database backend (sqlite)", defaultValue = "sqlite")
    private String db;

    @ConfigProperty(description = "Default data.db", defaultValue = "data.db")
    private String dbFile;

    @ConfigProperty(description = "Recorded sounds folder", defaultValue = "./recs/")
    private int searchLimit;

    @ConfigProperty(description = "Record tracks", defaultValue = "false")
    private boolean recorder;

    @ConfigProperty(description = "Recorded sounds folder", defaultValue = "./recs/")
    private String recordingsFolder;

    @ConfigProperty(description = "Item count per pages in queue command", defaultValue = "8")
    private int queueListItemCount;

    @ConfigProperty(description = "Maximum volume, sound distortion may occur when the volume is above 100", defaultValue = "100")
    private int maximumVolume;


}