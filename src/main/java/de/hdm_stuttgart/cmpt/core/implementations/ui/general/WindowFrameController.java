package de.hdm_stuttgart.cmpt.core.implementations.ui.general;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowFrameController implements Initializable {
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean windowMaximized = false;

    @FXML private BorderPane windowFrame;
    @FXML private HBox windowControls;
    @FXML private Button buttonMin;
    @FXML private Label labelMin;
    @FXML private Button buttonMax;
    @FXML private Label labelMax;
    @FXML private Button buttonClose;
    @FXML private Label labelClose;
    @FXML private BorderPane windowMove;
    @FXML private Label labelWindowTitle;
    @FXML private Polygon polygon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init CONST
        labelMin.setText(CONST.ICON_MIN);
        labelMax.setText(CONST.ICON_MAX);
        labelClose.setText(CONST.ICON_CLOSE);
        labelWindowTitle.setText(CONST.APP_NAME + " " + CONST.APP_TYPE);
        polygon.getPoints().addAll(
                0.0, 40.0,
                32.0, 40.0,
                32.0, 0.0);

        buttonMin.setOnAction(event -> {
            getAndSetStage();
            stage.setIconified(true);
        });
        buttonMax.setOnAction(event -> {
            getAndSetStage();
            if (windowMaximized) {
                stage.hide();
                stage.setWidth(CONST.MAIN_WINDOW_WIDTH);
                stage.setHeight(CONST.MAIN_WINDOW_HEIGHT);
                stage.centerOnScreen();
                stage.show();
                windowMaximized = false;
            } else {
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.hide();
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                stage.show();
                windowMaximized = true;
            }
        });
        buttonClose.setOnAction(event -> {
            getAndSetStage();
            stage.close();
        });
        windowMove.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        windowMove.setOnMouseDragged(event -> {
            getAndSetStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void getAndSetStage() {
        if (stage == null) stage = (Stage) windowFrame.getScene().getWindow();
    }

    public void setWindowTitle(String title) {
        labelWindowTitle.setText(title);
    }

    public void removeResizeControls() {
        windowControls.getStyleClass().add("small");
        windowControls.getChildren().removeAll(
                buttonMax,
                buttonMin
        );
    }

    public Button getButtonClose() {
        return buttonClose;
    }
}
