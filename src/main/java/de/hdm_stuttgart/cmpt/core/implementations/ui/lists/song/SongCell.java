package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.song;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SongCell extends ListCell<Song> {
    private static Logger log = LogManager.getLogger(SongCell.class);

    private boolean hovered = false;

    @FXML private BorderPane songCell;
    @FXML private Label title;
    @FXML private Label artist;
    @FXML private Label icon;
    @FXML private Button buttonFavorite;

    SongCell() {
        super();
        // load fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/fxml/lists/song-cell.fxml")
            );
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            log.error("Could not load fxml.");
        }

        // init states
        setHoveredState();

        // EVENTS
        // double click
        log.trace("set on mouse clicked.");
        this.setOnMouseClicked(event -> {
            if (getItem() != null && event.getClickCount() == 2) {
                UserInterfaceController.getInstance().playSong(getItem());
            }
        });
        // favor
        buttonFavorite.setOnAction(event -> toggleFavorState(getItem()));
        // hover
        songCell.setOnMouseEntered(event -> {
            hovered = true;
            setHoveredState();
        });
        songCell.setOnMouseExited(event -> {
            hovered = false;
            setHoveredState();
        });
    }

    @Override
    public void updateItem(Song item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null || !empty) {
            // values
            title.setText(getItem().getTitle());
            artist.setText(getItem().getArtist() + " • " + getItem().getAlbum());

            // remove classes
            songCell.getStyleClass().remove("playing");
            songCell.getStyleClass().remove("first-child");
            buttonFavorite.getStyleClass().remove("active");

            // add conditional classes
            if (getIndex() == 0) songCell.getStyleClass().add("first-child");
            if (getItem().equals(
                    UserInterfaceController.getInstance().getCurrentSong()
            )) songCell.getStyleClass().add("playing");

            setFavorState();

            // finally
            setGraphic(songCell);
        } else {
            // empty item
            setGraphic(null);
        }
    }

    private void setHoveredState() {
        if (hovered) icon.setText(CONST.ICON_PLAY);
        else icon.setText(CONST.ICON_SONG);
    }

    private void toggleFavorState(Song item) {
        log.trace("toggle favorite state");
        if (UserInterfaceController.getInstance().getFavoredPlaylist().getSongs().contains(item)) {
            log.trace("Song in favorite songs, remove it.");
            UserInterfaceController.getInstance().removeSongFromFavored(item);
        } else {
            log.trace("Song not in favorite songs, add it.");
            UserInterfaceController.getInstance().addSongToFavored(item);
        }
        setFavorState();
    }

    private void setFavorState() {
        if (getItem() != null && UserInterfaceController.getInstance()
                .getFavoredPlaylist().getSongs().contains(getItem())) {
            buttonFavorite.setText(CONST.ICON_HEART_FILLED);
            buttonFavorite.getStyleClass().add("active");
        } else {
            buttonFavorite.setText(CONST.ICON_HEART_EMPTY);
            buttonFavorite.getStyleClass().remove("active");
        }
    }
}
