package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.loader;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class InitLoaderController implements Initializable {
    private static Logger log = LogManager.getLogger(InitLoaderController.class);

    @FXML private BorderPane loader;
    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;
    @FXML private ProgressBar progressBar;
    @FXML private Label labelLoading;
    @FXML private Label labelContent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.debug("initializing InitLoader");
        labelLoading.setText(CONST.LOADING + "...");
        labelContent.setText(CONST.APP_START);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        log.trace("InitLoader initialized");
    }

    public WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }
}

