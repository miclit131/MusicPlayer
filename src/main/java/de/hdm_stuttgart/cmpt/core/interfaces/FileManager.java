package de.hdm_stuttgart.cmpt.core.interfaces;

import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileManager {


    enum FileManagerGroups{
        LOCAL,
        REMOTE;
        FileManagerGroups() {}
    }

    String[] ACCEPTED_MUSIC_FILE_EXTENSIONS = {"mp3"};

    String PLAYLIST_FILE_EXTENSION = ".pl";
    String MEDIALIBRARY_FILE_EXTENSION = ".ml";
    String CONFIGURATION_FILE_EXTENSION = ".conf";

    String PLAYLIST_DEFAULT_DIRECTORY = "storage" + File.separatorChar + "playlists" + File.separatorChar;
    String MEDIALIBRARY_DEFAULT_DIRECTORY = "storage" + File.separatorChar;
    String CONFIGURATION_DEFAULT_DIRECTORY = "storage" + File.separatorChar;

    String MEDIALIBRARY_DEFAULT_FILE_NAME = "library";
    String SETTINGS_DEFAULT_FILE_NAME = "app";

    String THEMES = "themes";
    String THEME_NAME = "name";
    String THEME_PATH = "path";
    String THEME_ACTIVE = "theme_active";

    String MEDIALIBRARY_DEFAULT_FILE_PATH =
            MEDIALIBRARY_DEFAULT_DIRECTORY + MEDIALIBRARY_DEFAULT_FILE_NAME + MEDIALIBRARY_FILE_EXTENSION;
    String SETTINGS_DEFAULT_FILE_PATH =
            CONFIGURATION_DEFAULT_DIRECTORY + SETTINGS_DEFAULT_FILE_NAME + CONFIGURATION_FILE_EXTENSION;

    File[] getPlaylistFiles() throws IOException;
    File[] getPlaylistFiles(String path) throws IOException;

    void writePlaylists(List<Playlist> playlists) throws IOException;

    void writePlaylist(Playlist playlistToWrite) throws IOException;

    MediaLibrary loadMediaLibrary() throws IOException;

    void writeMediaLibrary(MediaLibrary library) throws IOException;

    Song loadSong(File songFile) throws IOException;

    void setTheme(Theme theme) throws IOException;

    Theme[] getThemes();

    Theme getTheme() throws IOException;

    void waitToLoadLibrarySongs();

    void updatePlaylist(Playlist oldPlaylist, Playlist updatedPlaylist) throws IOException;
}
