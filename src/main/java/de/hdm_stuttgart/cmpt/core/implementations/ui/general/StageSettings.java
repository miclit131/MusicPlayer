package de.hdm_stuttgart.cmpt.core.implementations.ui.general;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageSettings {
    public static void defaultShow(Parent parent, Stage stage) {
        // set scene
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        // styles
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();
        stage.show();
        stage.requestFocus();
    }

    public static void modalShow(Parent parent, Stage stage) {
        stage.initModality(Modality.APPLICATION_MODAL);

        StageSettings.defaultShow(parent, stage);
    }

    public static void modalShowAndWait(Parent parent, Stage stage) {
        stage.initModality(Modality.APPLICATION_MODAL);
        // set scene
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        // styles
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();
        stage.requestFocus();
        stage.showAndWait();
    }
}
