package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.loader;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InitLoader {
    private static Logger log = LogManager.getLogger(InitLoader.class);

    private Stage primaryStage;
    private String[] fonts = {
            "/font/line-awesome/fonts/line-awesome.ttf",
            "/font/overpass/overpass-400.ttf",
            "/font/overpass/overpass-600.ttf",
            "/font/overpass/overpass-mono.ttf"
    };

    public InitLoader() throws Exception {
        primaryStage = new Stage();
        log.debug("Starting InitLoader");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/stages/loader-init.fxml"));
        Parent root = loader.load();
        log.debug("FXML loaded");

        log.debug("Adding stylesheet");
        root.getStylesheets().add(getClass().getResource(
                UserInterfaceController.getTheme().getFilePath()
        ).toExternalForm());

        log.debug("Loading fonts");
        loadFonts();

        InitLoaderController controller = loader.getController();
        controller.getWindowFrameController().setWindowTitle(CONST.APP_NAME);
        controller.getWindowFrameController().removeResizeControls();

        primaryStage.setTitle(CONST.APP_START);

        log.debug("Showing init loading stage");
        StageSettings.defaultShow(root, primaryStage);

        controller.getWindowFrameController().getButtonClose().setOnAction(event -> {
            Platform.exit();
        });
    }

    public void close() {
        primaryStage.close();
    }

    private void loadFonts() {
        for (String font : fonts) {
            log.debug("loading font: " + font);
            Font.loadFont(getClass().getResourceAsStream(font), 10);
        }
    }
}
