package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.nav;

import java.util.ArrayList;

public class Nav extends ArrayList<Nav> {
    public final static int PLAYLISTS = 0;
    public final static int SONGS = 1;
    public final static int FAVORITE = 2;
    public final static int GENRES = 3;
    public final static int ALBUMS = 4;
    public final static int ARTISTS = 5;
    public final static int ABOUT = 6;
    public final static int SETTINGS = 7;
    public final static int PLAYING = 8;

    private String title;
    private String icon;
    private int id;

    public Nav(int id, String title, String icon) {
        this.title = title;
        this.icon = icon;
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }
}
