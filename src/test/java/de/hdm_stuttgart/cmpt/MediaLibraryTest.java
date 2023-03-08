package de.hdm_stuttgart.cmpt;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibraryManager;
import org.json.JSONArray;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MediaLibraryTest {

    @Test
    public void testToString() throws InstantiationException {
        MainController controller = new MainController();
        MediaLibraryManager manager = MediaLibraryManager.getInstance();
        List<String> paths = new ArrayList<>();
        paths.add("ExamplePath");
        paths.add("This is a path");
        MediaLibrary library = new MediaLibrary(manager, paths, null);
        assertEquals("The toString method doesn't return the expected String.",
                "[\"ExamplePath\",\"This is a path\"]",
                library.toString());
    }

    @Test
    public void testSongsToJSONArray() {
        MainController controller = new MainController();
        MediaLibraryManager manager = null;
        try {
            manager = MediaLibraryManager.getInstance();
        } catch (InstantiationException e) {
            fail( "Could not get Controller instance");
            e.printStackTrace();
        }
        List<String> paths = new ArrayList<>();
        paths.add("ExamplePath");
        paths.add("This is a path");
        List<Song> songs =  new ArrayList<>();
        songs.add(new Song("Title","Artist", "Album", 1,1,1970, 100,"filepath"));
        songs.add(new Song("Title2","Artist2", "Album2", 2,2,1971, 101,"filepath2"));
        MediaLibrary library = new MediaLibrary(manager, paths, songs);
        assertEquals(
                "The songPathsToJSONArray method doesn't return the expected String.",
                new JSONArray("[\"filepath\",\"filepath2\"]").toString(),
                library.songPathsToJSONArray().toString());
    }
}
