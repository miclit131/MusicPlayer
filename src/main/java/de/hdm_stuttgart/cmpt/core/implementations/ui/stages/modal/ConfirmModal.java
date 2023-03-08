package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfirmModal {
    private static Logger log = LogManager.getLogger(ConfirmModal.class);

    private ConfirmModalController controller;

    public static final int STYLE_DELETE = 0;
    public static final int STYLE_ACCEPT = 1;

    public ConfirmModal(int CONFIRM_MODAL_STYLE, String title, String question, String action, String confirm) {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        // GET ROOT
        Parent root = null;
        try {
            loader.setLocation(getClass().getResource("/fxml/stages/confirm-modal.fxml"));
            log.debug("loaded fxml");
            root = loader.load();
        } catch (Exception e) {
            new InfoModal(
                    InfoModal.STYLE_ERROR,
                    CONST.COULD_NOT_LOAD_WINDOW[0] +
                            CONST.COULD_NOT_LOAD_WINDOW[1],
                    e.getMessage()
            );
            log.error(e.getMessage());
        }
        // GET CONTROLLER
        controller = loader.getController();
        controller.setStageValues(CONFIRM_MODAL_STYLE, question, action, confirm);
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getTheme().getFilePath()
        ).toExternalForm());
        // CREATE STAGE
        Stage stage = new Stage();
        stage.setTitle(title);
        controller.setStage(stage);
        controller.getWindowFrameController().setWindowTitle(title);

        StageSettings.modalShowAndWait(root, stage);
    }

    public boolean isConfirmed() {
        return controller.isConfirmed();
    }
}
