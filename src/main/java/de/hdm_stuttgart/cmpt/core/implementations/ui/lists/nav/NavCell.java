package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.nav;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NavCell extends ListCell<Nav> {
    private static Logger log = LogManager.getLogger(NavCell.class);

    @FXML private HBox navCell;
    @FXML private Label title;
    @FXML private Label icon;

    NavCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lists/nav-cell.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            log.debug("Could not load fxml");
        }
    }

    @Override
    public void updateItem(Nav item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null || !empty) {
            // values
            title.setText(getItem().getTitle());
            icon.setText(getItem().getIcon());

            // finally
            setGraphic(navCell);
        } else {
            // empty item
            setGraphic(null);
        }
    }
}
