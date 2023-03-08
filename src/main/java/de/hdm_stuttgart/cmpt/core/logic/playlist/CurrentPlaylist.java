package de.hdm_stuttgart.cmpt.core.logic.playlist;

import de.hdm_stuttgart.cmpt.core.logic.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurrentPlaylist extends Playlist {

    public List<Song> orderedlist;
    public List<Song> randomlist = new ArrayList<>();

    public CurrentPlaylist(String name, ArrayList<Song> songlist) {
        super(name, songlist);
        this.orderedlist = getSongs();
    }

    public CurrentPlaylist(Playlist playlist) {
        super(playlist.getName(), playlist.getSongs());
        this.orderedlist = playlist.getSongs();
    }

    public Song getSong (int index, boolean shuffle) {
        // TODO: Fix Index Out of Bounds Exception
        if (shuffle) return this.randomlist.get(index);
        if (this.orderedlist.isEmpty()) return null;
        return this.orderedlist.get(index);
    }


    public void randomizePlaylist() {
        Random ran = new Random();
        int[] indexes = new int[getSongs().size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        for (int i = 0; i < indexes.length; i++) {
            int change = i + ran.nextInt(indexes.length - i);
            int helper = indexes[i];
            indexes[i] = indexes[change];
            indexes[change] = helper;
        }
        for (int i = 0; i < orderedlist.size(); i++) {
            randomlist.add(orderedlist.get(indexes[i]));
        }

    }

}
