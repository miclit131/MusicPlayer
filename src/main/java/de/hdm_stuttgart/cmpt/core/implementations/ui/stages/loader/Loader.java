package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.loader;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Loader {
    private static Logger log = LogManager.getLogger(Loader.class);

    private LoaderController controller;
    private Stage stage;

    public Loader(String loadingContentName) throws Exception {
        // FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/stages/loader.fxml"));
        // GET ROOT
        Parent root = loader.load();
        log.debug("loaded fxml");
        // GET CONTROLLER
        controller = loader.getController();
        // CSS
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getTheme().getFilePath()
        ).toExternalForm());
        log.debug("loaded css");
        // CREATE STAGE
        stage = new Stage();
        stage.setTitle(CONST.APP_NAME);
        controller.setValues(loadingContentName);
        controller.getWindowFrameController().setWindowTitle(CONST.APP_NAME);
        controller.getWindowFrameController().removeResizeControls();

        StageSettings.defaultShow(root, stage);

        controller.getWindowFrameController().getButtonClose().setOnAction(event -> {
            // TODO: cancel event
            stage.close();
        });
    }

    public LoaderController getController() {
        return controller;
    }

    public Stage getStage() {
        return stage;
    }
}
