package me.koply.pikap.database.api;

import me.koply.pikap.database.model.*;

import java.util.List;
import java.util.Map;

public interface Database {

    boolean connect(Map<String, String> config);
    void close();
    boolean isFileDB();

    void createTrack(Track track);
    void createTrackIfNotExists(Track track);
    void updateTrack(Track track);
    void deleteTrack(Track track);
    Track queryTrackById(int id);
    Track queryTrackByIdentifier(String identifier);
    List<Track> queryAllTracks();
    List<Track> queryTracksByIds(int[] ids);

    void createFavoriteIfNotExists(FavouriteTrack track);
    void deleteFavorite(FavouriteTrack track);
    List<FavouriteTrack> queryAllFavorites();

    void createPlaylist(Playlist playlist);
    void updatePlaylist(Playlist playlist);
    List<Playlist> queryAllPlaylists();
    Playlist queryPlaylistById(int id);
    Playlist queryPlaylistByIdentifier(String identifier);

    void createPlayedPlaylist(PlayedPlaylist playlist);
    void updatePlayedPlaylist(PlayedPlaylist playlist);
    PlayedPlaylist queryPlayedPlaylistById(int id);

}