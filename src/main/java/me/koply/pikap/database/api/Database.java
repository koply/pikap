package me.koply.pikap.database.api;

import me.koply.pikap.database.model.FavouriteTrack;
import me.koply.pikap.database.model.Track;

import java.util.List;
import java.util.Map;

public interface Database {

    boolean connect(Map<String, String> config);
    void close();
    boolean isFileDB();

    void initializeListeners();

    void createTrack(Track track);
    void createTrackIfNotExists(Track track);
    void deleteTrack(Track track);
    Track queryTrackById(int id);
    Track queryTrackByIdentifier(String identifier);
    List<Track> queryAllTracks();

    void createFavoriteIfNotExists(FavouriteTrack track);
    void deleteFavorite(FavouriteTrack track);
    List<FavouriteTrack> queryAllFavorites();
}