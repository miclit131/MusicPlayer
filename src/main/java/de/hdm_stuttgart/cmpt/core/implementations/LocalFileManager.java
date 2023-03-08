package de.hdm_stuttgart.cmpt.core.implementations;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalFileManager implements FileManager {

    private static Logger log = LogManager.getLogger(LocalFileManager.class);

    private MediaLibrary library;
    private Theme activeTheme;
    private Theme[] themes;

    private List<Thread> threadsForLoadingSongs = new ArrayList<>();

    private Thread songsLoadingThread = createSongsLoadingThread();

    private Thread themesLoadingThread = createNewThemeLoadingThread();

    private Thread themeWritingThread = createNewThemeWritingThread();

    private static LocalFileManager localFileManager;

    private LocalFileManager() {
        loadThemes();
    }

    protected static LocalFileManager getInstance() {
        if(MainController.getInstance() == null) {
            log.warn("No instance of main controller found.");
        }
        if(localFileManager == null) {
            localFileManager = new LocalFileManager();
            localFileManager.threadsForLoadingSongs = new ArrayList<>();
        }
        return localFileManager;
    }

    /**
     * Returns all Files in the PLAYLIST_DEFAULT_DIRECTORY as File [].
     * This assumes that all Files in the Directory are Playlist Files (no Filter).
     *
     * @return a File [] with all Files
     */
    @Override
    public File[] getPlaylistFiles() {
        return getPlaylistFiles(PLAYLIST_DEFAULT_DIRECTORY);
    }

    /**
     * Returns Playlist Files in the directory you specified by the Parameter path.
     * Does filter the files first by file extension using PLAYLIST_FILE_EXTENSION.
     *
     * @param path the directory you want to search for PlaylistFiles.
     * @return a File [] with all Files that have the PLAYLIST_FILE_EXTENSION
     */
    @Override
    synchronized public File[] getPlaylistFiles(String path) {
        log.trace("Getting playlist files from " + path + "...");
        File directory = new File(path);

        if(!directory.isDirectory()) {
            log.warn("Directory for playlists " + path + " does not exist.");
            log.debug("Create directory for playlists...");
            if(directory.mkdirs()) {
                log.debug("Directory for playlists created.");
            }else {
                log.warn("Directory (" + FileManager.PLAYLIST_DEFAULT_DIRECTORY
                        + ") for playlists could not been created.");
            }
            log.warn("Return empty list of files.");
            return new File[0];
        }
        // COMPLETED Use FileFilter to filter the files from a path
        // TODO Test and log this method
        return directory.listFiles((dir, name) -> name.endsWith(PLAYLIST_FILE_EXTENSION));
    }

    /**
     * reads the File stored in MEDIALIBRARY_DEFAULT_FILE_PATH by Building a String and parsing that String to a JSONArray.
     * Only works, if the File Content is a JSONArray, otherwise it will throw a JSONException .
     *
     * @return the MediaLibrary the file in MEDIALIBRARY_DEFAULT_FILE_PATH contains, if it in fact is a MediaLibrary. Returns Null instead.
     *
     */
    @Override
    synchronized public MediaLibrary loadMediaLibrary() throws IOException {
        log.info("Load media library file...");


        // Get media library object
        String fileContent = readFile(MEDIALIBRARY_DEFAULT_FILE_PATH);

        try {
            // Convert content of file to json array
            JSONArray array = new JSONArray(fileContent);
            library = new MediaLibrary(array);

            if(array.length() == 0) {
                log.warn("Media library found but is empty.");
                library = createDefaultMediaLibrary();
            }
        } catch(JSONException e) {
            log.warn("Could not parse String to JSON Array.");
            library = createDefaultMediaLibrary();
        }

        // Start thread for loading songs
        if(songsLoadingThread.getState() == Thread.State.NEW ||
                songsLoadingThread.getState() == Thread.State.TERMINATED) {
            songsLoadingThread.start();
        } else {
            log.warn("Songs loading thread is probably running or waiting.");
            log.warn("Loading songs is skipped.");
        }
        return library;
    }

    private void loadAllSongsInMultipleThreads(MediaLibrary library) {
        // Run a thread that fetches also the data of songs from paths and add to mediaLibrary
        waitForSearchingThreads();
        threadsForLoadingSongs = new ArrayList<>();
        for(String path : library.getPaths()) {
            Thread t = new Thread(() -> {
                log.debug("Searching for songs in path "+ path);
                List<Song> songs = getSongsFromPath(path);
                log.trace("Add songs to media library...");
                library.addSongs(songs);
                log.debug("Search thread in path " + path + " completed.");
            });
            threadsForLoadingSongs.add(t);
            t.start();
        }
        log.trace("Starting all searching threads...");
    }

    /**
     * TODO Write doc comments
     * @param path
     * @return
     */
    private List<Song> getSongsFromPath(String path) {
        // TODO Test and log this method
        File directory = new File(path);
        List<Song> songs = new ArrayList<>();
        FileUtils.listFiles(directory, ACCEPTED_MUSIC_FILE_EXTENSIONS, true)
                .forEach((file) -> songs.add(loadSong(file)));
        if(songs.size() > 0) {
            log.info("Songs found in " + path + " and returned.");
        }else {
            log.warn("No songs found in " + path + ".");
        }
        return songs;
    }

    @Override
    public Song loadSong(File songFile) {
        log.trace("Load song " + songFile +"...");
        Mp3File mp3 = null;
        try {
            mp3 = new Mp3File(songFile);
        } catch (IOException e) {
            log.error("Reading file " + songFile + " failed.");
        } catch (UnsupportedTagException e) {
            log.error("Tag from file " + songFile + " is no supported.");
            e.printStackTrace();
        } catch (InvalidDataException e) {
            log.error("File " + songFile + " contains invalid data. (Message: " + e.getMessage() + ")");
        }
        if(mp3 == null) {
            log.warn("Ignore file " + songFile);
            return null;
        }
        ID3v1 id3;
        if (mp3.hasId3v2Tag()) {
            id3 = mp3.getId3v2Tag();
        } else if(mp3.hasId3v1Tag()) {
            id3 = mp3.getId3v1Tag();
        }  else {
            log.warn("No id3 tag found in file "+ songFile);
            // TODO Return song with default info.
            return null;
        }
        String title = id3.getTitle();
        String artist = id3.getArtist();
        String album = id3.getAlbum();
        int track = 0;

        try {
            track = Integer.parseInt(id3.getTrack());
        } catch (NumberFormatException e) {
            log.warn("Null or invalidtrack information in mp3 tag from file " + songFile);
        }

        int year = 0;
        try {
            year = Integer.parseInt(id3.getYear());
        } catch (NumberFormatException e) {
            log.warn("Null or invalid year information in mp3 tag from file " + songFile);
        }
        int genre = id3.getGenre();
        log.trace("Data from song " + songFile + "fetched. Create and return Song.");
        return new Song(title, artist, album, track, genre, year, mp3.getLengthInMilliseconds(), songFile.toString());
    }

    @Override
    public void setTheme(Theme theme) {
        activeTheme = theme;
        new Thread(() -> {
            try {
                themeWritingThread.join();
            } catch (InterruptedException e) {
                log.warn("Thread who writes theme into file was iinterrupted.");
            }
            themeWritingThread = createNewThemeWritingThread();
            themeWritingThread.start();
        }).start();
    }

    private void loadThemes() {
        themesLoadingThread.start();
    }

    @Override
    public Theme[] getThemes() {
        try {
            themesLoadingThread.join();
        } catch (InterruptedException e) {
            // TODO Log
            e.printStackTrace();
        }
        return themes;
    }

    @Override
    public Theme getTheme() {
        try {
            themesLoadingThread.join();
        } catch (InterruptedException e) {
            // TODO Log
            e.printStackTrace();
        }
        return activeTheme;
    }

    /**
     * TODO Write doc comments
     * @param library
     * @return
     */
    @Override
    synchronized public void writeMediaLibrary(MediaLibrary library) throws IOException {
        JSONArray array = library.toJSONArray();
        writeFile(MEDIALIBRARY_DEFAULT_FILE_NAME, array.toString());
    }

    @Override
    public void waitToLoadLibrarySongs() {
        if(songsLoadingThread.isAlive()) {
            log.trace("songsLoadingThread is alive.");
            try {
                log.trace("Wait to start all search processes...");
                songsLoadingThread.join();
                log.trace("All searching processes started.");
            } catch (InterruptedException e) {
                log.warn("The thread starting all search processes was interrupted. (Message: " + e.getMessage() + ")");
            }
        } else {
            log.debug("songsLoadingThread is not alive.");
        }
        waitForSearchingThreads();
    }

    @Override
    public void updatePlaylist(Playlist oldPlaylist, Playlist updatedPlaylist) throws IOException {
        // TODO Implement me
    }

    private void waitForSearchingThreads() {
        if(threadsForLoadingSongs.size() > 0) {
            log.trace("Threads to wait for: " + threadsForLoadingSongs.size());
            for (Thread t : threadsForLoadingSongs) {
                if (t.isAlive()) {
                    try {
                        log.trace("Waiting for " + threadsForLoadingSongs.size() + " thread(s)...");
                        t.join();

                    } catch (InterruptedException e) {
                        log.warn("A searching thread was interrupted. Message: " + e.getMessage());
                    }
                }
            }
        }
    }

    private MediaLibrary createDefaultMediaLibrary() {
        log.info("Create media library with user's default music directory...");
        String homeDirectory = System.getProperty("user.home");
        // TODO Change the following directories to the default OS directories of the music folder of user
        log.trace("Home directory created: " + StringEscapeUtils.escapeJava(homeDirectory));
        JSONArray array = new JSONArray("[\"" + StringEscapeUtils.escapeJava(homeDirectory) + StringEscapeUtils.escapeJava(""+File.separatorChar) + "Music\"]\n");
        return  new MediaLibrary(array);
    }

    @Override
    public void writePlaylists(List<Playlist> playlists) throws IOException {
        for(Playlist playlist : playlists) {
            writePlaylist(playlist);
        }
    }

    @Override
    public void writePlaylist(Playlist playlistToWrite) throws IOException {

        String playlistContent = playlistToWrite.toJSONObject().toString();
        String playlistName = playlistToWrite.getName();
        log.trace("Writing playlist " + playlistName + " to storage.");
        String playlistFileName = PLAYLIST_DEFAULT_DIRECTORY + playlistName + PLAYLIST_FILE_EXTENSION;
        writeFile(playlistFileName, playlistContent);
        log.trace("Playlist " + playlistName + " written to storage.");
    }


    // TODO Log this method
    synchronized private void writeFile(String fileName, String content) throws IOException {
        // Replace all whitespaces in file name with undercores
        fileName = fileName.replace(" ", "_");

        File file = new File(fileName);
        if(!file.mkdirs()) throw new IOException("Directories to file \"" + fileName + "\" could not been created.");
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    // TODO Log this method
    synchronized private String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            log.warn("File " + fileName + " could not be found.");
        }
        return content.toString();
    }


    /* * * * * * * * * * * * * */
    /* Thread initializations  */
    /* * * * * * * * * * * * * */

    private Thread createNewThemeWritingThread() {
        return new Thread(() -> {
            JSONObject object = new JSONObject();
            object.put(THEME_ACTIVE, activeTheme.toString());
            JSONArray themeArray = new JSONArray();
            for(Theme t : themes) {
                themeArray.put(t.toString());
            }
            object.put(THEMES, themeArray);
            try {
                writeFile(SETTINGS_DEFAULT_FILE_PATH, object.toString());
            } catch (IOException e) {
                log.error("Active theme could not been saved persistently.");
            }
        });
    }

    private Thread createNewThemeLoadingThread() {
        return new Thread(() -> {
            try {
                String themesString = readFile(SETTINGS_DEFAULT_FILE_PATH);
                JSONObject obj = new JSONObject(themesString);
                JSONObject themeObject = obj.getJSONObject(THEME_ACTIVE);
                activeTheme = new Theme(themeObject);
                JSONArray array = obj.getJSONArray(THEMES);
                Theme[] temp = new Theme[array.length()];
                for(int i = 0; i < array.length(); i++) {
                    temp[i] = new Theme(array.getJSONObject(i));
                }
                themes = temp;
            } catch(IOException e) {
                log.error("Themes could not been loaded.");
            }
        });
    }

    private Thread createSongsLoadingThread() {
        return new Thread(() -> {
            log.warn("Start loading songs from media library paths...");
            loadAllSongsInMultipleThreads(library);
            log.info("Loading songs from media library completed.");
        });
    }
}
