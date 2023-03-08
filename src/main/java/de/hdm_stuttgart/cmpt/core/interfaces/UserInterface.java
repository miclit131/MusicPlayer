package de.hdm_stuttgart.cmpt.core.interfaces;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface UserInterface {

    Stage getStage();
    Parent getRoot();
    void showInfoModal(int INFO_MODAL_STYLE, String message, String text);
    void updateCurrentSongInfo();
}

