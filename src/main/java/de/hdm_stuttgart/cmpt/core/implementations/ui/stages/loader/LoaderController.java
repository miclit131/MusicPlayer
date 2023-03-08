package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.loader;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoaderController implements Initializable {
    @FXML private BorderPane loader;
    @FXML private ProgressBar progressBar;
    @FXML private Label labelLoading;
    @FXML private Label labelContent;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelLoading.setText(CONST.LOADING + "...");
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }

    public void setValues(String loadingContentName) {
        labelContent.setText(loadingContentName);
    }
}
