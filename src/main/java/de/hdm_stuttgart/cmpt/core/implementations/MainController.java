package de.hdm_stuttgart.cmpt.core.implementations;

import de.hdm_stuttgart.cmpt.core.exceptions.WrongFileManagerException;
import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.interfaces.Controller;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.interfaces.UserInterface;
import de.hdm_stuttgart.cmpt.core.logic.Player;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibraryManager;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import de.hdm_stuttgart.cmpt.core.logic.playlist.PlaylistManager;
import de.hdm_stuttgart.cmpt.core.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.List;


public final class MainController implements Controller {

    private static Logger log = LogManager.getLogger(MainController.class);

    private FileManager fileManager;
    private MediaLibraryManager mediaLibraryManager;
    private PlaylistManager playlistManager;
    private UserInterface userInterface;
    private static MainController mainController;
    private Player player;

    public Thread libraryReadThread = createLibraryReadThread();

    public Thread libraryWriteThread = createLibraryWriteThread();


    private Thread playlistsReadThread = createPlaylistReadThread();

    /**
     * TODO Write doc comments
     */
    public void start() {
        // TODO Check order and completeness
        loadMediaLibrary();
        loadPlaylists();
        startGUI();
    }

    public MainController() {
        mainController = this;
        // Initialize a multithread file manager.


        try {
            this.fileManager = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.LOCAL);
            this.mediaLibraryManager = MediaLibraryManager.getInstance();
            this.playlistManager = PlaylistManager.getInstance();

        } catch (InstantiationException e) {
            // TODO Handle the isntantiation exception thrown
        } catch (WrongFileManagerException e) {
            log.error(e.getMessage());
            log.fatal("Wrong file manager parsed to factory. Application cannot start.");
            System.exit(1);
        }
    }

    public static MainController getInstance() {
        if (mainController == null)
            // TODO Check who is first creating new instance of controller. If not App, then exit.
            // TODO Log (Trace) or Log(ERROR) depending on access
            mainController = new MainController();
        return mainController;
    }

    @Override
    public void close() {
        // TODO interrupt all reading threads when user closes window.
        // TODO Wait until all threads finished (write-threads pool and read threads)
        // TODO Close the application properly
        // TODO - Music has to be stopped
        // TODO - The managers have to  be persist last data
        // TODO - The last playing playlist and song has to be persist for next run of MusicPlayer
        System.exit(0);
    }

    @Override
    public void loadMediaLibrary() {
        libraryReadThread.start();
    }

    @Override
    public void loadPlaylists() {
        playlistsReadThread.start();
    }

    @Override
    public Player getPlayer() {
        // TODO Returns the created player,
        // for example to give a reference to the player to the GUI
        // which executes play, stop, pause etc.
        if(player == null) {
            player = new Player();
        }
        return player;
    }

    @Override
    public List<Song> loadSongs(MediaLibrary mediaLibrary) { // MediaLibrary doesn't have to be parsed
        // TODO Request from file manager the songs from media library paths and returns those
        return null;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public void addLibraryPath(String path) {
        // TODO Do some logging or check if path is correct etc.
        mediaLibraryManager.addPathToMediaLibrary(path);
    }

    @Override
    public void removeLibraryPath(String path) {
        // TODO Do some logging or check if possible to remove path
        //mediaLibraryManager.removePathFromMediaLibrary(path);
    }

    @Override
    public MediaLibrary getMediaLibrary() {
        // TODO May log?
        return mediaLibraryManager.getMediaLibrary();
    }

    @Override
    public Theme[] getThemes() {
        return fileManager.getThemes();
    }

    @Override
    public Theme getTheme() {
        // TODO Return the active theme
        return new Theme(CONST.THEME_DARK,  "/css/dark.css");
    }

    @Override
    public void setTheme(Theme theme) {
        // TODO Change the active theme (filemanager)
    }

    @Override
    public void createPlaylist(Playlist newPlaylist) {
        // TODO Create a thread pool and add this thread to pool
        new Thread(() -> {
            try {
                playlistManager.addPlaylist(newPlaylist);
                fileManager.waitToLoadLibrarySongs();
                fileManager.writePlaylist(newPlaylist);
                // TODO Notify UI for changes
            } catch (IOException e) {
                // TODO Log this thread
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public List<Playlist> getPlaylists() {
        // TODO Maybe wait for playlistManager to load the playlists

        log.trace("Amount of playlists found: " + playlistManager.getPlaylists().size());
        return playlistManager.getPlaylists();
    }

    @Override
    public void updatePlaylist(Playlist oldPlaylist, Playlist updatedPlaylist) {
        // TODO Create a thread pool and add this thread to pool
        playlistManager.removePlaylist(oldPlaylist);
        playlistManager.addPlaylist(updatedPlaylist);
        new Thread (() -> {
            try {
                fileManager.updatePlaylist(oldPlaylist, updatedPlaylist);
                // TODO Notify UI to update playlist
            } catch (IOException e) {
                // TODO Log any exceptions
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void deletePlaylist(Playlist playlistToDelete) {
        // TODO Implement deletion of playlist through filemanager.
        playlistManager.removePlaylist(playlistToDelete);
        // TODO Remove from hard disk too
    }

    private void startGUI() {
        // TODO Remove this thread and run the method / initialization only after the backend finished loading
        try {
            libraryReadThread.join();
            playlistsReadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileManager.waitToLoadLibrarySongs();
        userInterface = UserInterfaceController.getInstance();
    }


    private Thread createLibraryReadThread() {
        return new Thread(() -> {
            // TODO Log this thread
            // COMPLETED get from the fileManager the medialibrary file
            MediaLibrary library = null;
            try {
                library = fileManager.loadMediaLibrary();
            } catch (IOException e) {
                // TODO Handle exception
                e.printStackTrace();
            }
            mediaLibraryManager.setMediaLibrary(library);
            mediaLibraryManager.loadLists();
        });
    }

    private Thread createLibraryWriteThread() {
        return new Thread(() -> {
            // TODO Log this thread
            MediaLibrary library = mediaLibraryManager.getMediaLibrary();
            try {
                fileManager.writeMediaLibrary(library);
                // TODO Notify UI for changes
            } catch (IOException e) {
                // TODO Handle exception
                e.printStackTrace();
            }
        });
    }
    private Thread createPlaylistReadThread() {
        return new Thread(() -> {
            // TODO Log this thread
            File[] files = new File[0];
            try {
                files = fileManager.getPlaylistFiles();
            } catch (IOException e) {
                // TODO Handle exception
                e.printStackTrace();
            }
            JSONArray playlistArray = Converter.filesToJSONArray(List.of(files));
            // Wait until the library reading thread and all its sub thread finished loading the MediaLibrary
            try {
                libraryReadThread.join();
            } catch (InterruptedException e) {
                // TODO Handle interruption
                e.printStackTrace();
            }
            playlistManager.loadPlaylists(playlistArray);
        });
    }
}
