package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoModal {
    public static final int STYLE_ERROR = 0;
    public static final int STYLE_SUCCESS = 1;
    public static final int STYLE_INFO = 2;

    public InfoModal(int INFO_MODAL_STYLE, String message, String text) {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/stages/info-modal.fxml"));
        // GET ROOT
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // GET CONTROLLER
        InfoModalController controller = loader.getController();
        controller.setStageValues(INFO_MODAL_STYLE, message, text);
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getTheme().getFilePath()
        ).toExternalForm());
        // CREATE STAGE
        Stage stage = new Stage();
        controller.setStage(stage);
        switch (INFO_MODAL_STYLE) {
            case STYLE_ERROR:
                stage.setTitle(CONST.ERROR);
                controller.getWindowFrameController().setWindowTitle(CONST.ERROR_OCCURED);
                break;
            case STYLE_SUCCESS:
                stage.setTitle(CONST.SUCCESS);
                controller.getWindowFrameController().setWindowTitle(CONST.SUCCESS);
                break;
            case STYLE_INFO:
                stage.setTitle(CONST.INFO);
                controller.getWindowFrameController().setWindowTitle(CONST.INFO);
                break;
        }
        StageSettings.modalShow(root, stage);
    }
}
