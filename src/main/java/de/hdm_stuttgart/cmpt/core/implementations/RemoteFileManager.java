package de.hdm_stuttgart.cmpt.core.implementations;

import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.List;

// TODO Implement a single thread file manager
public class RemoteFileManager implements FileManager {

    private static RemoteFileManager remoteFileManager;

    protected static RemoteFileManager getInstance() {
        // TODO Implement me
        return remoteFileManager;
    }

    @Override
    public File[] getPlaylistFiles() {
        // TODO Implement me
        return new File[0];
    }

    @Override
    public File[] getPlaylistFiles(String path) {
        // TODO Implement me
        return new File[0];
    }

    @Override
    public void writePlaylists(List<Playlist> playlists) {
        // TODO Implement me
    }

    @Override
    public void writePlaylist(Playlist playlistToWrite) throws IOException {
        // TODO implement me
    }

    @Override
    public MediaLibrary loadMediaLibrary() {
        // TODO Implement me
        return null;
    }

    @Override
    public void writeMediaLibrary(MediaLibrary library) {
        // TODO Implement me
    }

    @Override
    public Song loadSong(File songFile) {
        // TODO Implement me
        return null;
    }

    @Override
    public void setTheme(Theme theme) throws IOException {
        // TODO Implement me
    }

    @Override
    public Theme[] getThemes() {
        // TODO implement me
        return null;
    }

    @Override
    public Theme getTheme() throws IOException {
        // TODO Implement me
        return null;
    }

    @Override
    public void waitToLoadLibrarySongs() {
        // If the RemoteFileManager runs in different thread
        // wait for this thread, else leave empty
    }

    @Override
    public void updatePlaylist(Playlist oldPlaylist, Playlist updatedPlaylist) throws IOException {
        // TODO Implement me
    }
}
