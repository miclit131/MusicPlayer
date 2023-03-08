package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.settings;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InfoModal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Settings {
    private static Logger log = LogManager.getLogger(Settings.class);

    public Settings() {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        // GET ROOT
        Parent root = null;
        try {
            loader.setLocation(getClass().getResource("/fxml/stages/settings.fxml"));
            log.debug("loaded fxml");
            root = loader.load();
        } catch (Exception e) {
            new InfoModal(
                    InfoModal.STYLE_ERROR,
                    CONST.COULD_NOT_LOAD_WINDOW[0] +
                            " '" + CONST.SETTINGS + "'\n" +
                            CONST.COULD_NOT_LOAD_WINDOW[1],
                    e.getMessage()
            );
            log.error(e.getMessage());
        }
        log.debug("loaded fxml");
        // GET CONTROLLER
        SettingsController settingsController = loader.getController();
        settingsController.setRoot(root);
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getInstance().getTheme().getFilePath()
        ).toExternalForm());
        log.debug("loaded css");
        // CREATE STAGE
        Stage stage = new Stage();
        stage.setTitle(CONST.SETTINGS);
        settingsController.setStage(stage);
        settingsController.getWindowFrameController().setWindowTitle(CONST.SETTINGS);

        StageSettings.modalShow(root, stage);
    }
}
