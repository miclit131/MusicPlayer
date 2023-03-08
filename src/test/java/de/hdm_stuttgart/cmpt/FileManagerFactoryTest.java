package de.hdm_stuttgart.cmpt;

import de.hdm_stuttgart.cmpt.core.exceptions.WrongFileManagerException;
import de.hdm_stuttgart.cmpt.core.implementations.FileManagerFactory;
import de.hdm_stuttgart.cmpt.core.implementations.LocalFileManager;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;

import static org.junit.Assert.*;
import org.junit.Test;



public class FileManagerFactoryTest {

    @Test
    public void testCreateFileManager_01() throws WrongFileManagerException {
        FileManager f = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.LOCAL);
        assertTrue("Wrong file manager returned.", f instanceof LocalFileManager);
    }

    @Test (expected = WrongFileManagerException.class)
    public void testCreateFileManager_02() throws WrongFileManagerException {
        FileManager f = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.REMOTE);
    }

    /**
     * Multiple executions have to result to the same object
     * @throws WrongFileManagerException
     */
    @Test
    public void testCreateFileManager_03() throws WrongFileManagerException {
        FileManager f = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.LOCAL);
        FileManager f2 = FileManagerFactory.createFileManager(FileManager.FileManagerGroups.LOCAL);
        assertEquals("The file managers returned are not the same instance.", f, f2);
    }
}
