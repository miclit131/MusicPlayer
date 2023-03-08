package de.hdm_stuttgart.cmpt.core.logic.library;

//import com.sun.istack.internal.Nullable;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaLibrary {

    private MediaLibraryManager mediaLibrarymanager;

    /**
     * A list with all paths saved to media library
     */
    private List<String> paths;

    /**
     * A list of all songs
     */
    private List<Song> songs;

    public MediaLibrary(MediaLibraryManager manager, List<String> paths, List<Song> songs) {
        this.mediaLibrarymanager = manager;
        this.paths = paths;
        this.songs = new ArrayList<>();
        if(songs != null) {
            this.songs.addAll(songs);
        }
    }

    public MediaLibrary(JSONArray paths) {
        this.paths = new ArrayList<>();
        for(Object c : paths.toList()) {
            if(c instanceof String) {
                this.paths.add((String)c);
            }
        }
        this.songs = new ArrayList<>();
    }


    public List<String> getPaths() {
        return paths;
    }

    public List<Song> getSongs() {
        FileManager manager = MainController.getInstance().getFileManager();
        manager.waitToLoadLibrarySongs();
        return songs;
    }

    void addPaths(String ... paths) {
        this.paths.addAll(Arrays.asList(paths));
    }

    void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    synchronized void addSong(Song song) {
        songs.add(song);
    }

    synchronized public void addSongs(List<Song> songList) {
        songs.addAll(songList);
    }

    @Override
    public String toString() {
        return new JSONArray(paths).toString();
    }

    public JSONArray songPathsToJSONArray() {
        JSONArray songsInLibrary = new JSONArray();
        for(Song song : songs) {
            songsInLibrary.put(song.toString());
        }
        return songsInLibrary;
    }

    /**
     * Returns the first {@link Song} where its filepath is equals the pathname given.
     *
     * @param filepath the filepath to look for
     * @return returns a {@link Song} object if filepath matched to a songs path, null otherwise.
     */
    Song getSongByPath(String filepath) {
        for(Song song : songs) {
            if(song.getFilePath().equals(filepath)) return song;
        }
        return null;
    }

    public JSONArray toJSONArray() {
        return new JSONArray(paths);
    }
}
