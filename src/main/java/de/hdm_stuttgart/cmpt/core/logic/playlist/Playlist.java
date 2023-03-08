package de.hdm_stuttgart.cmpt.core.logic.playlist;

import de.hdm_stuttgart.cmpt.core.logic.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Playlist extends ArrayList<Playlist> {

    // STATIC
    // Logger
    private static Logger log = LogManager.getLogger(Playlist.class);
    // Playlist Types
    public static final int TYPE_NORMAL  = 0;
    public static final int TYPE_FAVORED = 1;
    // Sort Types
    public static final int SORT_NAME       = 0;
    public static final int SORT_CREATED_AT = 1;
    public static final int SORT_LAST_HEARD = 2;
    // JSON Object
    private static final String JSON_TYPE       = "type";
    private static final String JSON_SONGS      = "songs";
    private static final String JSON_LAST_SONG  = "last_song";
    private static final String JSON_NAME       = "name";
    private static final String JSON_CREATED_AT = "created_at";
    private static final String JSON_LAST_HEARD = "last_heard";
    // Date Format
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    // Comparators
    public final static Comparator<? super Playlist> comparatorSortName = (Comparator<Playlist>) Playlist::compareToByName;
    public final static Comparator<? super Playlist> comparatorSortCreatedAt = (Comparator<Playlist>) Playlist::compareToByCreatedAt;
    public final static Comparator<? super Playlist> comparatorSortLastHeard = (Comparator<Playlist>) Playlist::compareToByLastHeard;

    // OBJECTIVE
    private String            name;
    private Map<String, Song> songMap;
    private Calendar          createdAt;
    private Calendar          lastHeard;
    private int               numberOfSongs;
    private Song              lastSong;
    private int               type;

    // CONSTRUCTORS
    // TODO Add a reference to the PlaylistManager
    public Playlist(String name, List<Song> songs) {
        this.type = TYPE_NORMAL;
        Calendar cal = new GregorianCalendar();
        this.name = name;
        setSongs(songs);
        this.createdAt = cal;
        this.lastHeard = cal;
        this.lastSong = null;
    }

    public Playlist(int TYPE, String name, List<Song> songs) {
        this.type = TYPE;
        Calendar cal = new GregorianCalendar();
        this.name = name;
        setSongs(songs);
        this.createdAt = cal;
        this.lastHeard = cal;
        this.lastSong = null;
    }

    public Playlist(int TYPE, String name, Map<String, Song> songMap) {
        this.type = TYPE;
        Calendar cal = new GregorianCalendar();
        this.name = name;
        this.songMap = songMap;
        this.createdAt = cal;
        this.lastHeard = cal;
        this.lastSong = null;
    }

    // TODO Add a reference to the PlaylistManager
    public Playlist(String          name,
                    Map<String, Song> songMap,
                    Calendar        createdAt,
                    Calendar        lastHeard,
                    Song            lastSong) {
        this.type = TYPE_NORMAL;
        this.name = name;
        this.songMap = songMap;
        this.numberOfSongs = songMap.size();
        this.createdAt = createdAt;
        this.lastHeard = lastHeard;
        this.lastSong = lastSong;
    }

    public Playlist(JSONObject object) {
        JSONArray songArray = (JSONArray) object.get(JSON_SONGS);

        this.name = (String) object.get(JSON_NAME);

        this.songMap = createMapWithJSONArray(songArray);
        // TODO songs not defined yet, maybe replace with map.
        try {
            this.createdAt.setTime(df.parse((String) object.get(JSON_CREATED_AT)));
            this.lastHeard.setTime(df.parse((String) object.get(JSON_LAST_HEARD)));
        } catch (ParseException e) {
            log.error("Could not parse one or many dateFormats for Playlist '" + name + "'.");
        }
        this.lastSong = songMap.get(JSON_LAST_SONG);
        this.type = (int) object.get(JSON_TYPE);
    }

    // GETTER

    public List<Song> getSongs() {
        return new ArrayList<>(songMap.values());
    }

    Map<String, Song> getSongMap() {
        return songMap;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    private Calendar getCreatedAt() {
        return createdAt;
    }

    private Calendar getLastHeard() {
        return lastHeard;
    }

    public Song getLastSong() {
        return lastSong;
    }

    private Set<String> getSongPaths() {
        return songMap.keySet();
    }

    // SETTER

    public void setLastHeard(Calendar lastHeard) {
        this.lastHeard = lastHeard;
    }

    public void setSongs(List<Song> newSongs) {
        Map<String, Song> tempMap = new HashMap<>();
        int i = 0;
        for (Song song: newSongs) {
            tempMap.put(song.getFilePath(), song);
            i++;
        }
        this.songMap = tempMap;
        numberOfSongs = i;
    }

    // JSON

    /**
     * Converts this {@link Playlist} to {@link JSONObject}
     *
     * @return returns Playlist as JSONObject
     */
    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put(JSON_NAME,       name);
        object.put(JSON_SONGS,      songsToJSONArray());
        object.put(JSON_CREATED_AT, df.format(createdAt.getTime()));
        object.put(JSON_LAST_HEARD, df.format(lastHeard.getTime()));
        object.put(JSON_LAST_SONG,  lastSong.getFilePath());
        object.put(JSON_TYPE,       type);
        return object;
    }

    private JSONArray songsToJSONArray() {
        JSONArray songsInPlaylist = new JSONArray();
        songsInPlaylist.put(getSongPaths());
        return songsInPlaylist;
    }

    private Map<String, Song> createMapWithJSONArray(JSONArray array) {
        Map<String, Song> songMap = new HashMap<>(array.length());
        for(Object a : array) {
            if(a instanceof String) {
                songMap.put((String)a, null);
            }
        }
        return songMap;
    }

    // Comparision

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof Playlist) {
            final Playlist that = (Playlist) o;
            if(this.name.hashCode() == that.getName().hashCode()) {
                return this.name.equals(that.getName());
            }
        }
        return false;
    }

    private int compareToByName(Playlist playlist) {
        return this.getName().compareTo(playlist.getName());
    }

    private int compareToByCreatedAt(Playlist playlist) {
        return createdAt.compareTo(playlist.getCreatedAt());
    }

    private int compareToByLastHeard(Playlist playlist) {
        return lastHeard.compareTo(playlist.getLastHeard());
    }
}
