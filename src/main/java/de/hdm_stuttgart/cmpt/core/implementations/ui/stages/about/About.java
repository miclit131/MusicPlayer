package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.about;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InfoModal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class About {
    private static Logger log = LogManager.getLogger(About.class);

    private AboutController controller;

    public About() {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        // GET ROOT
        Parent root = null;
        try {
            loader.setLocation(getClass().getResource("/fxml/stages/about.fxml"));
            log.debug("loaded fxml");
            root = loader.load();
        } catch (Exception e) {
            new InfoModal(
                    InfoModal.STYLE_ERROR,
                    CONST.COULD_NOT_LOAD_WINDOW[0] +
                            " '" + CONST.ABOUT + "'\n" +
                            CONST.COULD_NOT_LOAD_WINDOW[1],
                    e.getMessage()
            );
            log.error(e.getMessage());
        }
        log.debug("loaded fxml");
        // GET CONTROLLER
        controller = loader.getController();
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getTheme().getFilePath()
        ).toExternalForm());
        log.debug("loaded css");
        // CREATE STAGE
        Stage stage = new Stage();
        stage.setTitle(CONST.ABOUT + " " + CONST.APP_NAME);
        controller.setStage(stage);
        controller.getWindowFrameController().setWindowTitle(CONST.ABOUT + " " + CONST.APP_NAME);
        controller.getWindowFrameController().removeResizeControls();

        StageSettings.modalShow(root, stage);
    }
}
