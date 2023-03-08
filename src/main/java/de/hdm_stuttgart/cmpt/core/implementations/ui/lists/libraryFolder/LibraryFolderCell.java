package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.settings.SettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibraryFolderCell extends ListCell<LibraryFolder> {
    private static Logger log = LogManager.getLogger(LibraryFolderCell.class);

    @FXML private BorderPane folderCell;
    @FXML private Label title;
    @FXML private Label location;
    @FXML private Label icon;
    @FXML private Button buttonRemove;

    LibraryFolderCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lists/library-folder-cell.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            log.error("Could not load fxml.");
        }
    }

    @Override
    public void updateItem(LibraryFolder item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null || !empty) {
            icon.setText(CONST.ICON_FOLDER);
            String[] folders = item.getLocation().split("[/\\|]");
            title.setText(folders[folders.length - 1]);
            location.setText(item.getLocation());
            buttonRemove.setText(CONST.ICON_CLOSE);

            setGraphic(folderCell);

            buttonRemove.setOnAction(event -> {
                SettingsController.getInstance().removeLibrary(item);
            });
        } else {
            setGraphic(null);
        }
    }
}
