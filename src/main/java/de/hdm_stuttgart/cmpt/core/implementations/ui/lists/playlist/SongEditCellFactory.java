package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist;

import de.hdm_stuttgart.cmpt.core.logic.Song;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SongEditCellFactory implements Callback<ListView<Song>, ListCell<Song>> {
    @Override
    public ListCell<Song> call(ListView<Song> listview) {
        return new SongEditCell();
    }
}