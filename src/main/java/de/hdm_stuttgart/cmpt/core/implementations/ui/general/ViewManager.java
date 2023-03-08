package de.hdm_stuttgart.cmpt.core.implementations.ui.general;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ViewManager {
    public static Button createButton(String icon, String title) {
        Button button = new Button();
        button.getStyleClass().addAll("app-button", "transparent");
        HBox hBox = new HBox();
        Label bIcon = new Label(icon);
        bIcon.getStyleClass().add("app-icon");
        Label bLabel = new Label(title);
        hBox.getChildren().addAll(bIcon, bLabel);
        button.setGraphic(hBox);
        BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
        return button;
    }

    public static Button createIconButton(String icon) {
        Button button = new Button(icon);
        button.getStyleClass().addAll("app-button", "transparent", "icon");
        BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
        return button;
    }

    public static Button createBackButton() {
        Button button = new Button(CONST.ICON_BACK);
        button.getStyleClass().add("button-back");
        button.getStyleClass().add("icon");
        BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
        return button;
    }

    public static void setSortButtons(HBox groupSortingButtons, RadioButton[] radioButtons) {
        ToggleGroup toggleGroup = new ToggleGroup();
        for (RadioButton rb : radioButtons) {
            groupSortingButtons.getChildren().add(rb);
            rb.setToggleGroup(toggleGroup);
        }
        toggleGroup.selectToggle(radioButtons[0]);
    }
}
