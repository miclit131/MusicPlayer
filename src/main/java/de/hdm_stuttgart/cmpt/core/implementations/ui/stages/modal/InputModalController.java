package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal;

import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class InputModalController implements Initializable {
    private static Logger log = LogManager.getLogger(InputModalController.class);

    private Stage stage;
    private boolean isSet = false;

    @FXML private TextField textField;
    @FXML private Button commitButton;
    @FXML private Label commitButtonLabel;
    @FXML private Label commitButtonIcon;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        windowFrameController.removeResizeControls();
        Platform.runLater(() -> textField.requestFocus());
        // events
        this.commitButton.setOnAction(event -> closeOrFail(this.textField.getText()));
        this.textField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                closeOrFail(this.textField.getText());
            }
        });
    }

    private void closeOrFail(String text) {
        if (!text.isEmpty()) {
            this.isSet = true;
            this.stage.close();
        } else {
            log.warn("textField is empty.");
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setButton(String text, String icon) {
        this.commitButtonLabel.setText(text);
        this.commitButtonIcon.setText(icon);
    }

    boolean isSet() {
        return this.isSet;
    }

    String getTextFieldText() {
        return this.textField.getText();
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }
}
