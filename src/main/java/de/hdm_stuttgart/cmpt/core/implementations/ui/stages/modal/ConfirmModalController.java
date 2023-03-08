package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmModalController implements Initializable {
    private Stage stage;
    private boolean confirmed = false;

    @FXML private Button buttonCancel;
    @FXML private Button buttonConfirm;
    @FXML private Label labelQuestion;
    @FXML private Label labelAction;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        windowFrameController.removeResizeControls();
        Platform.runLater(() -> buttonConfirm.requestFocus());

        buttonCancel.setOnAction(event -> stage.close());
        buttonConfirm.setOnAction(event -> {
            confirmed = true;
            stage.close();
        });
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }

    void setStageValues(int STYLE, String question, String action, String confirm) {
        labelQuestion.setText(question);
        labelAction.setText(action);
        buttonCancel.setText(CONST.CANCEL);
        buttonConfirm.setText(confirm);
        switch (STYLE) {
            case ConfirmModal.STYLE_DELETE:
                buttonConfirm.getStyleClass().add("delete");
                break;
            case ConfirmModal.STYLE_ACCEPT:
                buttonConfirm.getStyleClass().add("accept");
                break;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
