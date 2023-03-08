package de.hdm_stuttgart.cmpt.core.implementations;

import de.hdm_stuttgart.cmpt.core.exceptions.WrongFileManagerException;
import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;

public class FileManagerFactory {


    public static FileManager createFileManager(FileManager.FileManagerGroups f) throws WrongFileManagerException{
        switch(f) {
            case LOCAL:
                return LocalFileManager.getInstance();
            case REMOTE:
                //return RemoteFileManager.getInstance();
            default:
                throw new WrongFileManagerException(
                        "Other file managers are not supported yet.");
        }
    }
}
