package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.playlistEdit;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InfoModal;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PlaylistEdit {
    private static Logger log = LogManager.getLogger(PlaylistEdit.class);

    public PlaylistEdit(String playlistName, List<Song> playlistSongs) {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        // GET ROOT
        Parent root = null;
        try {
            loader.setLocation(getClass().getResource("/fxml/stages/playlist-edit.fxml"));
            log.debug("loaded fxml");
            root = loader.load();
        } catch (Exception e) {
            new InfoModal(
                    InfoModal.STYLE_ERROR,
                    CONST.COULD_NOT_LOAD_WINDOW[0] +
                            "\n'" + CONST.PLAYLIST_EDIT + "'\n" +
                            CONST.COULD_NOT_LOAD_WINDOW[1],
                    e.getMessage()
            );
            log.error(e.getMessage());
        }
        // GET CONTROLLER
        PlaylistEditController playlistEditController = loader.getController();
        playlistEditController.setPlaylist(playlistName, playlistSongs);
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getInstance().getTheme().getFilePath()
        ).toExternalForm());
        log.debug("loaded css");
        // CREATE STAGE
        Stage stage = new Stage();
        stage.setTitle(CONST.PLAYLIST_EDIT);
        playlistEditController.setStage(stage);
        playlistEditController.getWindowFrameController().setWindowTitle(CONST.PLAYLIST_EDIT);

        StageSettings.modalShow(root, stage);
    }
}
