package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "recordedtracks")
public class RecordedTrack {

    @DatabaseField(id = true)
    private int id;

    // knowntrack's identifier that points the YouTube key of the track
    @DatabaseField private String trackIdentifier;
    @DatabaseField private String fileName;


    public RecordedTrack() { }


}