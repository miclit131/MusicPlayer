package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoModalController implements Initializable {
    private Stage stage;

    @FXML private Button buttonOk;
    @FXML private BorderPane infoModal;
    @FXML private VBox content;
    @FXML private Label labelMessage;
    @FXML private Label labelHint;
    @FXML private Label labelText;
    @FXML private Label labelInfoIcon;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        windowFrameController.removeResizeControls();
        Platform.runLater(() -> buttonOk.requestFocus());

        buttonOk.setText(CONST.OKAY);
        buttonOk.setOnAction(event -> stage.close());
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }

    void setStageValues(int STYLE, String message, String text) {
        labelMessage.setText(message);
        if (text != null) labelText.setText(text);
        else content.getChildren().remove(labelText);
        switch (STYLE) {
            case InfoModal.STYLE_ERROR:
                labelInfoIcon.setText(CONST.ICON_WARNING);
                labelHint.setText(CONST.ERROR_LOG + ":");
                infoModal.getStyleClass().add("error");
                buttonOk.getStyleClass().add("delete");
                break;
            case InfoModal.STYLE_SUCCESS:
                labelInfoIcon.setText(CONST.ICON_CHECKED);
                content.getChildren().remove(labelHint);
                infoModal.getStyleClass().add("success");
                buttonOk.getStyleClass().add("accept");
                break;
            case InfoModal.STYLE_INFO:
                labelInfoIcon.setText(CONST.ICON_INFO);
                content.getChildren().remove(labelHint);
                break;
        }
    }
}
