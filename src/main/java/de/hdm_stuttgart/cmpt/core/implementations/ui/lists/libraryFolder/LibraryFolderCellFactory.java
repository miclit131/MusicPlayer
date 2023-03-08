package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LibraryFolderCellFactory implements Callback<ListView<LibraryFolder>, ListCell<LibraryFolder>> {
    @Override
    public ListCell<LibraryFolder> call(ListView<LibraryFolder> listview) {
        return new LibraryFolderCell();
    }
}