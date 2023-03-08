package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.playlistEdit.PlaylistEditController;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SongEditCell extends ListCell<Song> {
    private static Logger log = LogManager.getLogger(SongEditCell.class);

    @FXML private BorderPane songEditCell;
    @FXML private Label icon;
    @FXML private Label title;
    @FXML private Label artist;
    @FXML private Button buttonToggle;

    SongEditCell() {
        super();
        // load fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/fxml/lists/playlist-song-edit-cell.fxml")
            );
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            log.error("Could not load fxml.");
        }

        // init states
        setStateButtonIcon();

        // EVENTS
        buttonToggle.setOnAction(event -> {
            toggleAdded(getItem());
        });
    }

    @Override
    public void updateItem(Song item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null || !empty) {
            // values
            icon.setText(CONST.ICON_SONG);
            title.setText(item.getTitle());
            artist.setText(item.getArtist() + " • " + item.getAlbum());

            // remove classes
            songEditCell.getStyleClass().remove("first-child");
            buttonToggle.getStyleClass().remove("active");

            // conditional
            if (getIndex() == 0) songEditCell.getStyleClass().add("first-child");
            setStateButtonIcon();

            // finally
            setGraphic(songEditCell);
        } else {
            // empty item
            setGraphic(null);
        }
    }

    private void toggleAdded(Song song) {
        ArrayList<Song> playlistSongs = PlaylistEditController.getInstance().getPlaylistSongs();
        if (playlistSongs.contains(song)) playlistSongs.remove(song);
        else playlistSongs.add(song);
        updateItem(getItem(), false);
    }

    private void setStateButtonIcon() {
        if (PlaylistEditController
                .getInstance()
                .getPlaylistSongs()
                .contains(getItem())
                ) {
            buttonToggle.setText(CONST.ICON_CHECKED);
            buttonToggle.getStyleClass().add("active");
        } else {
            buttonToggle.setText(CONST.ICON_ADD);
        }
    }
}
