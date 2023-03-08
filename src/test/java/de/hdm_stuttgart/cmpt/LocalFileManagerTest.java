package de.hdm_stuttgart.cmpt;

import de.hdm_stuttgart.cmpt.core.exceptions.WrongFileManagerException;
import de.hdm_stuttgart.cmpt.core.implementations.FileManagerFactory;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.library.MediaLibrary;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.*;

public class LocalFileManagerTest {

    private static FileManager fm;

    @BeforeClass
    public static void initialize() {
        try {
            fm = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.LOCAL);
        } catch(WrongFileManagerException we) {
            fail("WronFileManagerException thrown.");
        }
    }

    @Test
    public void testLoadMediaLibrary() {
        MediaLibrary ml;
        try {
            ml = fm.loadMediaLibrary();
        } catch (IOException e) {
            fail("IOException thrown by reading the media library.");
            return;
        }

        File file = new File(FileManager.MEDIALIBRARY_DEFAULT_DIRECTORY);
        if(file.isDirectory() && Objects.requireNonNull(file.listFiles()).length > 0) {
            // TODO Implement the case when a library file exists
        } else {
            assertEquals("Wrong amount of libraries.", 1, ml.getPaths().size());
            assertEquals("Wrong path created.", "\"" + System.getProperty("user.home") +File.separatorChar + "Music\"",
                    ml.getPaths().get(0));
        }
    }

    @Test
    public void testGetThemes() {
        Theme[] themes = fm.getThemes();
        assertEquals("Amount of themes returned is wrong.", 4, themes.length);
        assertEquals("File path from first theme is wrong.", "/css/dark.css", themes[0].getFilePath());
        assertEquals("File path from second theme is wrong.", "/css/light.css", themes[1].getFilePath());
        assertEquals("File path from third theme is wrong.", "/css/blue.css", themes[2].getFilePath());
        assertEquals("File path from fourth theme is wrong.", "/css/red.css", themes[3].getFilePath());
    }

    // TODO Test read / write procedures from playlsits and medialibrary
}
