package de.hdm_stuttgart.cmpt.core.interfaces;

import de.hdm_stuttgart.cmpt.core.logic.Player;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;

import java.util.ArrayList;
import java.util.List;

public interface Controller {

    /**
     * Closes the application properly
     */
    void close();

    /**
     * Starts loading the media library from storage
     */
    void loadMediaLibrary();

    /**
     * Starts loading the playlists from storage
     */
    void loadPlaylists();

    /**
     * Returns an instance of a {@link Player}.
     *
     * @return returns a {@link Player}
     */
    // PLAYER
    /* S show   */ Player getPlayer();

    /**
     * (Re)Loads the songs from the media library paths
     * (file manager is used).
     *
     * @param mediaLibrary the {@link MediaLibrary} that stores the paths
     * @return returns a list of {@link Song} objects
     */
    List<Song> loadSongs(MediaLibrary mediaLibrary);

    /**
     * Returns an instance of a {@link FileManager}.
     *
     * @return returns a {@link FileManager}
     */
    FileManager getFileManager();


    /* * * * * * * * * * */
    /* For UserInterface */
    /* * * * * * * * * * */

    // PLAYLIST
    /* C create */ void createPlaylist(Playlist playlist);
    /* R read   */ List<Playlist> getPlaylists();
    /* U update */ void updatePlaylist(Playlist playlist, Playlist newPlaylist);
    /* D delete */ void deletePlaylist(Playlist playlist);

    // TODO  Create controller with theme support that extends the following methods?
    // TODO Useful when combined with factories and implementations
    // THEME
    /* R read   */ Theme[] getThemes();
    /* U update */ void setTheme(Theme theme);
    /* S show   */ Theme getTheme();

    // MEDIA LIBRARY
    /* R read   */ MediaLibrary getMediaLibrary();
    /* U update */ void addLibraryPath(String path);
    /* D delete */ void removeLibraryPath(String path);
}
