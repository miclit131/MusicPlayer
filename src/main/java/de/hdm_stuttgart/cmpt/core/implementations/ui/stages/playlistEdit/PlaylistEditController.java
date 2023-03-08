package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.playlistEdit;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist.SongEditCellFactory;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InputModal;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaylistEditController implements Initializable {
    private static Logger log = LogManager.getLogger(PlaylistEditController.class);

    private static PlaylistEditController instance;
    private Stage stage;

    private String playlistName;
    private ArrayList<Song> playlistSongs;

    @FXML private BorderPane playlistEdit;
    @FXML private ListView<Song> listViewSongs;
    @FXML private Label labelHeading;
    @FXML private Button buttonSave;
    @FXML private Button buttonRename;
    @FXML private Label buttonSaveIcon;
    @FXML private Label buttonSaveLabel;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    public static PlaylistEditController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        windowFrameController.removeResizeControls();

        buttonSaveIcon.setText(CONST.ICON_SAVE);
        buttonSaveLabel.setText(CONST.SAVE);
        buttonRename.setText(CONST.ICON_EDIT);
        listViewSongs.setCellFactory(new SongEditCellFactory());
        listViewSongs.setItems(
                FXCollections.observableList(
                        UserInterfaceController.getInstance().getSongs()
                )
        );

        Platform.runLater(() -> labelHeading.setText(playlistName));

        // events

        buttonSave.setOnAction(event -> {
            UserInterfaceController.getInstance().updateActivePlaylist(
                    new Playlist(
                            playlistName,
                            playlistSongs
                    )
            );
            stage.close();
        });

        buttonRename.setOnAction(event -> {
            try {
                log.debug("creating inputModal for renamePlaylist");
                InputModal playlistRenameModal = new InputModal(
                        CONST.PLAYLIST_RENAME,
                        CONST.RENAME,
                        CONST.ICON_SAVE
                );
                if (playlistRenameModal.isSet()) {
                    String newName = playlistRenameModal.getText();
                    playlistName = newName;
                    labelHeading.setText(newName);
                } else {
                    log.warn("playlist was not renamed");
                }
            } catch (Exception e) {
                log.error("Could not create or use inputModal");
                e.printStackTrace();
            }
        });
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }

    public ArrayList<Song> getPlaylistSongs() {
        return playlistSongs;
    }

    void setPlaylist(String playlistName, List<Song> playlistSongs) {
        this.playlistName = playlistName;
        this.playlistSongs = new ArrayList<>(playlistSongs);
    }
}
