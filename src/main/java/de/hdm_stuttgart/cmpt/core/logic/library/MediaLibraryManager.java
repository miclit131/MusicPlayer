package de.hdm_stuttgart.cmpt.core.logic.library;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaLibraryManager {

    private MediaLibrary mediaLibrary;

    private FileManager fileManager;

    /* * * * * * * * * * * * * NOTE  * * * * * * * * * * * */
    /* These lists are not made to save them persistently, */
    /* but for runtime performance.                        */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Map<String ,List<Song>> albumLists = new HashMap<>();
    private Map<String ,List<Song>> artistLists = new HashMap<>();
    private Map<Integer ,List<Song>> genreLists = new HashMap<>();
    private Map<String ,List<Song>> tempAlbumLists;
    private Map<String ,List<Song>> tempArtistLists;
    private Map<Integer ,List<Song>> tempGenreLists;

    // TODO Implement a singleton cunstructor to create only one instance of the MediaLibraryManager.
    private static MediaLibraryManager libraryManager;

    private Thread cachingListsThread = new Thread(() ->{
        fileManager.waitToLoadLibrarySongs();
        // Load songs to temporary lists and at the end reference to those lists
        tempAlbumLists = new HashMap<>();
        tempArtistLists = new HashMap<>();
        tempGenreLists = new HashMap<>();
        for(Song song : mediaLibrary.getSongs()) {
            addToArtistLists(song);
            addToGenreLists(song);
            addToAlbumLists(song);
        }
        // Reference to the temp lists
        albumLists = tempAlbumLists;
        artistLists = tempArtistLists;
        genreLists = tempGenreLists;

        // Clear temp lists pointers
        // This step is not necessary
        tempAlbumLists = null;
        tempArtistLists = null;
        tempGenreLists = null;
    });

    private MediaLibraryManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public static MediaLibraryManager getInstance() throws InstantiationException{
        if(libraryManager == null) {
            if(MainController.getInstance() == null)
                throw new InstantiationException("Create a new instance of MainController first.");
            FileManager fileManager = MainController.getInstance().getFileManager();
            libraryManager = new MediaLibraryManager(fileManager);
        }
        // TODO Maybe check if controller is different to handle it (log or something)
        return libraryManager;
    }

    /**
     * Reloads the songs from disk and refreshes the lists
     */
    void reloadSongs() {
        List<Song> songs = MainController.getInstance().loadSongs(mediaLibrary);
        // TODO Join here thread started in loadSongs from MainController.
        mediaLibrary.setSongs(songs);
        loadLists();
    }

    public void addPathToMediaLibrary(String path) {
        fileManager.waitToLoadLibrarySongs();
        mediaLibrary.addPaths(path);
        // TODO Scan for new songs in new path and update the lists
        // TODO Send update request to LocalFileManager.
    }

    void staticScanForChanges(){
        //TODO implement me (refresh SongList in each Library with LocalFileManager)
    }

    /**
     * Returns the media library reference
     *
     * @return the media library stored in the MediaLibraryManager
     */
    public MediaLibrary getMediaLibrary() {
        try {
            MainController.getInstance().libraryReadThread.join();
            return mediaLibrary;
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO Handle interruption
        }
        return null;
    }

    /**
     * (Re)Loads the artistLists, genreLists, albumLists and favoriteSongs.
     * Uses temporary lists to avoid accessto uncompleted lists.
     */
    public void loadLists() {

        if(cachingListsThread.getState() == Thread.State.NEW ||
                cachingListsThread.getState() == Thread.State.TERMINATED) {
            cachingListsThread.start();
        } else {
            // TODO Log that the cachingListsThread is running or waiting and cannot restarted
            // TODO Log that cahchingListsThread start is skipped
        }
    }

    /**
     * Checks if the artist of song is already a list in artist lists.
     * If so, add the song to the list. If the artist is not a list yet,
     * create the artist list and add the song to it. Note that the song is only added
     * into a temporary list. Reference to the temporary list when you are finished.
     *
     * @param song song where to get the artist info and which is added to list
     */
    private void addToArtistLists(Song song) {
        String artist = song.getArtist();

        if (tempArtistLists.containsKey(artist)) {
            tempArtistLists.get(artist).add(song);
        } else {
            tempArtistLists.put(artist, new ArrayList<>(){{add(song);}});
        }
    }

    /**
     * Checks if the genre of song is already a list in genre lists.
     * If so, add the song to the list of genre. If the genre has no list yet,
     * create the genre list and add the song to it. Note that the song is only added
     * into a temporary list. Reference to the temporary list when you are finished.
     *
     * @param song song where to get the genre info and which is added to list
     */
    private void addToGenreLists(Song song) {
        int genre = song.getGenre();

        if (tempGenreLists.containsKey(genre)) {
            tempGenreLists.get(genre).add(song);
        } else {
            tempGenreLists.put(genre, new ArrayList<>(){{add(song);}});
        }
    }

    /**
     * Checks if the album of song is already a list in album lists.
     * If so, add the song to the list of album. If the album has no list yet,
     * create the album list and add the song to it. Note that the song is only added
     * into a temporary list. Reference to the temporary list when you are finished.
     *
     * @param song song where to get the album info and which is added to list
     */
    private void addToAlbumLists(Song song) {
        String album = song.getAlbum();

        if (tempAlbumLists.containsKey(album)) {
            tempAlbumLists.get(album).add(song);
        } else {
            tempAlbumLists.put(album, new ArrayList<>(){{add(song);}});
        }
    }

    /**
     * Returns a list of all songs of the library
     *
     * @return a {@link List<Song>} object with all songs of the library
     */
    List<Song> getAllLibrarySongs() {
        return mediaLibrary.getSongs();
    }

    /**
     * TODO Write doc comments
     * @param list
     */
    public void fillSongList(Map<String, Song> list) {
        // TODO Run this in seperate thread
        // TODO Run this only if MediaLibrary songs loaded completely
        fileManager.waitToLoadLibrarySongs();
        for(String s : list.keySet()) {
            list.put(s, mediaLibrary.getSongByPath(s));
        }
    }

    public void setMediaLibrary(MediaLibrary library){
        this.mediaLibrary = library;
    }
}
