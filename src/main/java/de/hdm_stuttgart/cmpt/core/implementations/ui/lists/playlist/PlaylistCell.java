package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlaylistCell extends ListCell<Playlist> {
    private static Logger log = LogManager.getLogger(PlaylistCell.class);

    @FXML private BorderPane playlistCell;
    @FXML private Label title;
    @FXML private Label amount;
    @FXML private Label icon;

    PlaylistCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/fxml/lists/playlist-cell.fxml")
            );
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            log.debug("Could not load fxml.");
        }
    }

    @Override
    public void updateItem(Playlist item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null || !empty) {
            // values
            icon.setText(CONST.ICON_PLAYLIST);
            title.setText(item.getName());
            amount.setText(Integer.toString(item.getNumberOfSongs()) + " " + CONST.SONGS);

            // remove classes
            playlistCell.getStyleClass().remove("first-child");

            // conditional classed
            if (getIndex() == 0) playlistCell.getStyleClass().add("first-child");

            // finally
            setGraphic(playlistCell);
        } else {
            setGraphic(null);
        }
    }
}
