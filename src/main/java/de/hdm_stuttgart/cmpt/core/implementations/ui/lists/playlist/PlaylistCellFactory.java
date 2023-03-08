package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist;

import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PlaylistCellFactory implements Callback<ListView<Playlist>, ListCell<Playlist>> {
    @Override
    public ListCell<Playlist> call(ListView<Playlist> listview) {
        return new PlaylistCell();
    }
}