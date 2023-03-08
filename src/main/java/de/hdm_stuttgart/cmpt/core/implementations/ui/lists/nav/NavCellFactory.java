package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.nav;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NavCellFactory implements Callback<ListView<Nav>, ListCell<Nav>> {
    @Override
    public ListCell<Nav> call(ListView<Nav> listview) {
        return new NavCell();
    }
}