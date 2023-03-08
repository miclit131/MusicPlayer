package de.hdm_stuttgart.cmpt.core.logic;

import java.io.File;
import java.util.Comparator;

public class Song implements Comparable {

    /* Song Meta Information after ID3 */

    /**
     * 30 characters of the title (after ID3)
     */
    private String title;

    /**
     * 30 characters of the artist name
     */
    private String artist;

    /**
     * 30 characters of the album name (after ID3)
     */
    private String album;

    /**
     * The number of the track on the album, or 0.
     */
    private int track;

    /**
     * Index in a list of genres (after ID3)
     */
    private int genre;

    /**
     * A four-digit year (after ID3)
     */
    private int year;

    /* File specific data */

    /**
     * Track length in milliseconds
     */
    private double length;

    /**
     * Where the file is located, absolute Path
     */
    private String filePath;

    /*
     * Sorting IDs
     */
    public final static int SORT_TITLE = 0;
    public final static int SORT_ARTIST = 1;
    public final static int SORT_ALBUM = 2;
    public final static int SORT_YEAR = 3;

    /*
     * Comparators
     * for sorting Collections
     */
    public final static Comparator<? super Song> comparatorSortArtist = (Comparator<Song>) Song::compareToByArtist;
    public final static Comparator<? super Song> comparatorSortTitle = (Comparator<Song>) Song::compareToByTitle;
    public final static Comparator<? super Song> comparatorSortAlbum = (Comparator<Song>) Song::compareToByAlbum;
    public final static Comparator<? super Song> comparatorSortYear = (Comparator<Song>) Song::compareToByYear;

    public Song(String title, String artist, String album, int track,
                int genre, int year, double length, String filePath) {
        this.title = title != null ? title : new File(filePath).getName();
        this.artist = artist != null ? artist : "";
        this.album = album != null ? album : "";
        this.track = track;
        this.genre = genre;
        this.year = year;
        this.length = length;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getTrack() {
        return track;
    }

    public int getGenre() {
        return genre;
    }

    public String getGenreName() {
        return Genre.names[genre];
    }

    public int getYear() {
        return year;
    }

    public double getLength() {
        return length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Comparision

    public int compareToByTitle(Song song) {
        return this.title.compareTo(song.getTitle());
    }

    public int compareToByArtist(Song song) {
        return this.artist.compareTo(song.getArtist());
    }

    public int compareToByAlbum(Song song) {
        return this.album.compareTo(song.getAlbum());
    }

    public int compareToByYear(Song song) {
        return this.year - song.getYear();
    }

    public boolean equalsExplicit(Object o) {
        if (o instanceof Song) {
            final Song that = (Song) o;
            if (this.filePath.hashCode() == that.getFilePath().hashCode()) {
                return this.filePath.equals(that.getFilePath());
            }
        }
        return false;
    }

    // COMPLETED: Do much more comparisions
    // TODO Run the comparisons more efficient
    @Override
    public int compareTo(Object o) {
        if (o instanceof Song) {
            final Song that = (Song) o;
            // same file
            if (this.filePath.equals(that.filePath)) return 0;

            int h = compareToByTitle(that);
            int i = compareToByArtist(that);
            int j = compareToByAlbum(that);
            int k = that.track - this.track;
            int l = that.genre - this.genre;
            int m = compareToByYear(that);
            double n = this.length - that.length;
            // I hope you've get enough comparisons :D
            return h != 0 ? h : i != 0 ? i : j != 0 ? j : k != 0 ? k : l != 0 ? l : m != 0 ? m : n == 0 ? 0 : n > 0 ? 1 : -1;
        }
        return -9999;
    }

    @Override
    public int hashCode() {
        String s = this.filePath;
        return s.hashCode();
    }

    // TODO Check if returning only the filepath is enough or not
    @Override
    public String toString() {
        return filePath;
    }
}