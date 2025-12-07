package me.koply.pikap.database.impl;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.database.TableGenerator;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.database.repository.TrackRepository;
import me.koply.pikap.util.architecture.Delegate;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SQLiteTrackRepository implements TrackRepository {

    private final Delegate<Connection> connectionProvider;

    @Inject
    public SQLiteTrackRepository(Delegate<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
        initTable();
    }

    private void initTable() {
        try (Statement stmt = connectionProvider.get().createStatement()) {
            String query = TableGenerator.generateCreateTableSqlQuery(Track.class);
            stmt.execute(query);
            log.info("Table {} created successfully.", Track.class.getSimpleName().toLowerCase() + "s");
        } catch (SQLException e) {
            log.error("Failed to create table", e);
        }
    }

    @Override
    public Track save(Track track) {
        String query = "INSERT INTO tracks (title, author, identifier, duration, listenedTimes, lastMillis, lastPlayed) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connectionProvider.get().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, track.title());
            pstmt.setString(2, track.author());
            pstmt.setString(3, track.identifier());
            pstmt.setLong(4, track.duration());
            pstmt.setInt(5, track.listenedTimes());
            pstmt.setLong(6, track.lastMillis());
            pstmt.setLong(7, track.lastPlayed() != null ? track.lastPlayed().getTime() : 0);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        return new Track(newId, track.title(), track.author(), track.identifier(),
                                track.duration(), track.listenedTimes(), track.lastMillis(), track.lastPlayed());
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Failed to save track", e);
            // TODO: Exception handling
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM tracks WHERE id = ?";
        try (PreparedStatement pstmt = connectionProvider.get().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to delete track", e);
            // TODO: Exception handling
        }
    }

    @Override
    public Optional<Track> findById(Integer id) {
        String sql = "SELECT * FROM tracks WHERE id = ?";
        try (PreparedStatement pstmt = connectionProvider.get().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTrack(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find track", e);
            // TODO: Exception handling
        }
        return Optional.empty();
    }

    @Override
    public List<Track> findAll() {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT * FROM tracks";

        try (Statement stmt = connectionProvider.get().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tracks.add(mapRowToTrack(rs));
            }
        } catch (SQLException e) {
            log.error("Failed to find all tracks", e);
            // TODO: Exception handling
        }
        return tracks;
    }

    // ResultSet -> Track Record dönüşümü
    private Track mapRowToTrack(ResultSet rs) throws SQLException {
        return new Track(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("identifier"),
                rs.getLong("duration"),
                rs.getInt("listenedTimes"),
                rs.getLong("lastMillis"),
                new Timestamp(rs.getLong("lastPlayed"))
        );
    }
}
