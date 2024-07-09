package me.koply.pikap.database.dao;

import me.koply.pikap.database.model.FavouriteTrack;
import me.koply.pikap.database.model.Playlist;
import me.koply.pikap.database.model.Track;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface DatabaseAccessor {

    // TRACK
    void insertTrack(Track track);
    void insertTrack(Track track, Runnable onComplete);
    void upsertTrack(Track track);
    void upsertTrack(Track track, Runnable onComplete);
    void updateTrack(Track track);
    void updateTrack(Track track, Runnable onComplete);
    void deleteTrack(Track track);
    void deleteTrack(Track track, Runnable onComplete);
    void fetchTrackWhere(String column, Object object, Supplier<Track> onComplete);
    void fetchTrack(int id, Supplier<Track> onComplete);
    void fetchTrackByIdentifier(String identifier, Supplier<Track> onComplete);
    void fetchLastPlayedTrack(Supplier<Track> onComplete);
    void fetchAllTracks(Supplier<List<Track>> onComplete);
    void fetchMultipleTracks(Collection<Integer> ids, Supplier<List<Track>> onComplete);

    // FAV TRACK
    // TODO POSSIBLY REMOVE
    void insertFavouriteTrack(FavouriteTrack track);
    void insertFavouriteTrack(FavouriteTrack track, Runnable onComplete);
    void upsertFavouriteTrack(FavouriteTrack track);
    void upsertFavouriteTrack(FavouriteTrack track, Runnable onComplete);
    void updateFavouriteTrack(FavouriteTrack track);
    void updateFavouriteTrack(FavouriteTrack track, Runnable onComplete);
    void deleteFavouriteTrack(FavouriteTrack track);
    void deleteFavouriteTrack(FavouriteTrack track, Runnable onComplete);
    void fetchFavouriteTrackWhere(String column, Object object, Supplier<FavouriteTrack> onComplete);
    void fetchFavouriteTrack(int id, Supplier<FavouriteTrack> onComplete);
    void fetchAllFavouriteTracks(Supplier<List<FavouriteTrack>> onComplete);
    void fetchMultipleFavouriteTracks(Collection<Integer> ids, Supplier<List<FavouriteTrack>> onComplete);


    // PLAYLIST
    void insertPlaylist(Playlist playlist);
    void insertPlaylist(Playlist playlist, Runnable onComplete);
    void upsertPlaylist(Playlist playlist);
    void upsertPlaylist(Playlist playlist, Runnable onComplete);
    void updatePlaylist(Playlist playlist);
    void updatePlaylist(Playlist playlist, Runnable onComplete);
    void deletePlaylist(Playlist playlist);
    void deletePlaylist(Playlist playlist, Runnable onComplete);
    void fetchPlaylistWhere(String column, Object object, Supplier<Playlist> onComplete);
    void fetchPlaylist(int id, Supplier<Playlist> onComplete);
    void fetchPlaylistByIdentifier(String identifier, Supplier<Playlist> onComplete);
    void fetchAllPlaylists(Supplier<List<Playlist>> onComplete);
    void fetchMultiplePlaylists(Collection<Integer> ids, Supplier<List<Playlist>> onComplete);

    

}
