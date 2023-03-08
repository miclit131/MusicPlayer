package de.hdm_stuttgart.cmpt.core.implementations.ui;

/**
 * Final class holding constants
 */
public final class CONST {
    public final static String APP_NAME         = "Gramafon";
    public final static String APP_TYPE         = "Musik";
    public final static String APP_DESC         = "Entwickelt von";
    public final static String APP_VERSION      = "v0.3-fx";
    public final static String SHUFFLE          = "Zufall";
    public final static String PLAY             = "Abspielen";
    public final static String PLAYLISTS        = "Wiedergabelisten";
    public final static String PLAYLIST_NEW     = "Neue Wiedergabeliste";
    public final static String PLAYLIST_DELETE  = "Wiedergabeliste löschen";
    public final static String PLAYLIST_EDIT    = "Wiedergabeliste bearbeiten";
    public final static String PLAYLIST_RENAME  = "Wiedergabeliste umbenennen";
    public final static String RENAME           = "Umbenennen";
    public final static String CREATE           = "Erstellen";
    public final static String SONGS            = "Musiktitel";
    public final static String SONGS_ALL        = "Alle Titel";
    public final static String SONGS_ADD        = "Titel hinzufügen";
    public final static String SONG_NONE        = "Kein Titel";
    public final static String PLAYING_NOW      = "Aktuelle Wiedergabe";
    public final static String PLAYING_NONE     = "Keine Wiedergabe";
    public final static String ADD              = "Hinzufügen";
    public final static String ARTISTS          = "Künstler";
    public final static String ARTIST           = "Künstler";
    public final static String ALBUM            = "Album";
    public final static String ALBUMS           = "Alben";
    public final static String SETTINGS         = "Einstellungen";
    public final static String FOLDERS          = "Ordner";
    public final static String LIBRARY          = "Bibliothek";
    public final static String LIBRARIES        = "Bibliotheken";
    public final static String LIBRARY_REMOVE   = "Bibliothek entfernen";
    public final static String ABOUT            = "Über";
    public final static String GITLAB           = "Gitlab";
    public final static String FELGROM          = "Minesweeper spielen";
    public final static String CREATION_DATE    = "Erstellungsdatum";
    public final static String ALPHABETICAL     = "Alphabetisch";
    public final static String LAST_HEARD       = "Zuletzt gehört";
    public final static String YEAR             = "Jahr";
    public final static String FAVORITE         = "Favoriten";
    public final static String GENRES           = "Genres";
    public static final String CANCEL           = "Abbrechen";
    public static final String EDIT             = "Bearbeiten";
    public static final String LOADING          = "Ladevorgang";
    public static final String REMOVE           = "Entfernen";
    public static final String DELETE           = "Löschen";
    public static final String ACTIVE           = "Ausgewählt";
    public static final String THEME            = "Optik";
    public static final String APP_START        = "Anwendungsstart";
    public static final String SAVE             = "Speichern";
    public static final String NONE             = "Keinen";
    public static final String ONE              = "Einen";
    public static final String ALL              = "Alle";
    public static final String OKAY             = "Okay";
    public static final String ERROR            = "Fehler";
    public static final String ERROR_OCCURED    = "Es ist ein Fehler aufgetreten";
    public static final String SUCCESS          = "Erfolg";
    public static final String ERROR_LOG        = "Fehlerbericht";
    public static final String INFO             = "Information";
    public static final String ERROR_CRITICAL   = "Kritischer Fehler";
    public static final String COULD_NOT_START  = "Anwendung konnte nicht gestartet werden.\nGenauere Informationen wurden in der Log-Datei abgelegt.";

    public static final String THEME_DARK  = "Steingrau";
    public static final String THEME_LIGHT = "Kontrastreich";
    public static final String THEME_BLUE  = "Mitternachtsblau";
    public static final String THEME_RED   = "Weinrot";

    public static final String[] QUESTION_REMOVE = new String[]{
            "Möchten Sie",
            "entfernen?"
    };
    public static final String[] QUESTION_DELETE = new String[]{
            "Möchten Sie",
            "löschen?"
    };

    public static final String[] COULD_NOT_LOAD_WINDOW = new String[]{
            "Das Fenster",
            "konnte nicht geladen werden!"
    };

    public final static double MAIN_WINDOW_WIDTH  = 960;
    public final static double MAIN_WINDOW_HEIGHT = 640;

    public final static String ICON_ABOUT        = "\uF1EC";
    public final static String ICON_SETTINGS     = "\uF20C";
    public final static String ICON_PLAYLIST     = "\uF26C";
    public final static String ICON_SONG         = "\uF1E1";
    public final static String ICON_CLOSE        = "\uF342";
    public final static String ICON_MAX          = "\uF190";
    public final static String ICON_MIN          = "\uF28E";
    public final static String ICON_NEW          = "\uF2C4";
    public final static String ICON_PLAY         = "\uF2BE";
    public final static String ICON_PAUSE        = "\uF2AD";
    public final static String ICON_MALE         = "\uF27B";
    public final static String ICON_FAVORITE     = "\uF234";
    public final static String ICON_PLUS         = "\uF2C2";
    public final static String ICON_CHECKED      = "\uF17B";
    public final static String ICON_FOLDER       = "\uF1FE";
    public final static String ICON_DISK         = "\uF1BC";
    public final static String ICON_BACK         = "\uF120";
    public final static String ICON_ARTIST       = "\uF28C";
    public final static String ICON_GENRE        = "\uF2F1";
    public final static String ICON_SHUFFLE      = "\uF2D0";
    public final static String ICON_STEP_PREV    = "\uF31E";
    public final static String ICON_STEP_NEXT    = "\uF31F";
    public final static String ICON_REPEAT       = "\uF2D9";
    public final static String ICON_REFRESH      = "\uF2D5";
    public final static String ICON_EDIT         = "\uF2B0";
    public final static String ICON_SAVE         = "\uF2E9";
    public final static String ICON_ADD          = "\uF2C2";
    public final static String ICON_DELETE       = "\uF34D";
    public final static String ICON_VISUAL       = "\uF267";
    public final static String ICON_HEART_EMPTY  = "\uF234";
    public final static String ICON_HEART_FILLED = "\uF233";
    public final static String ICON_WARNING      = "\uF376";
    public final static String ICON_INFO         = "\uF24A";

    public static final int VIEW_SONGS_ALL      = 0;
    public static final int VIEW_SONGS_FAVORED  = 1;
    public static final int VIEW_PLAYLISTS      = 2;
    public static final int VIEW_PLAYLIST_SONGS = 3;
    public static final int VIEW_ALBUMS         = 4;
    public static final int VIEW_ALBUM_SONGS    = 5;
    public static final int VIEW_GENRES         = 6;
    public static final int VIEW_GENRE_SONGS    = 7;
    public static final int VIEW_ARTISTS        = 8;
    public static final int VIEW_ARTIST_SONGS   = 9;
    public static final int VIEW_PLAYING        = 10;

    /**
     * Private constructor to avoid object instantiation
     */
    private CONST() {}
}
