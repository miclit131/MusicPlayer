package de.hdm_stuttgart.cmpt.core.logic.playlist;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibraryManager;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistManager {

    private static PlaylistManager playlistManager;

    private Playlist currentPlaylist;
    private List<Playlist> playlists;
    private FileManager fileManager;

    public static PlaylistManager getInstance() throws InstantiationException {
        if(playlistManager == null) {
            if(MainController.getInstance() == null)
                throw new InstantiationException("Create a new instance of MainController first.");
            playlistManager = new PlaylistManager(MainController.getInstance().getFileManager());
            playlistManager.playlists = new ArrayList<>();
        }
        return playlistManager;
    }

    private PlaylistManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(Playlist playlistToPlay) {
        this.currentPlaylist = playlistToPlay;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> allPlaylists) {
        this.playlists = allPlaylists;
    }

    void deletePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
    }

    void createPlaylist(String name, List<Song> songs) {
        Playlist playlist = new Playlist(name, songs);
        this.playlists.add(playlist);
    }

    public void loadPlaylists(JSONArray playlistsArray) {
        for(int i = 0; i < playlistsArray.length(); i++) {
            // TODO Maybe run this process in a seperate thread
            this.playlists.add(new Playlist(playlistsArray.getJSONObject(i)));
        }
        loadSongsFromPlaylists();
    }

    private void loadSongsFromPlaylists() {
        fileManager.waitToLoadLibrarySongs();
        for(Playlist l : playlists) {
            try {
                MediaLibraryManager.getInstance().fillSongList(l.getSongMap());
            } catch (InstantiationException e) {
                // TODO Handle missing isntance of MediaLibraryManager
            }
        }
    }

    Map<String, Song> getSongsFromPaths(JSONArray array) {
        try {
            Map<String, Song> songMap = new HashMap<>(array.length());
            for(Object a : array) {
                if(a instanceof String) {
                    songMap.put((String)a, null);
                }
            }
            MediaLibraryManager.getInstance().fillSongList(songMap);
            return songMap;
        } catch (InstantiationException e) {
            // TODO Handle missing isntance of MediaLibraryManager
        }
        return null;
    }

    public void addPlaylist(Playlist playlistToAdd) {
        synchronized(playlists) {
            playlists.add(playlistToAdd);
        }
    }

    public void removePlaylist(Playlist oldPlaylist) {
        synchronized(playlists) {
            playlists.remove(oldPlaylist);
        }
    }
}
