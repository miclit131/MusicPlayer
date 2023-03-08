package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.settings;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.UserInterfaceController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder.LibraryFolder;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder.LibraryFolderCellFactory;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.ConfirmModal;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private static SettingsController instance;
    private static UserInterfaceController parentController;
    private Stage stage;
    private Parent root;

    private ObservableList<LibraryFolder> libraryFolderObservableList;

    @FXML private ListView<LibraryFolder> libraryListView;
    @FXML private ComboBox<Theme> comboBoxTheme;
    @FXML private BorderPane settings;
    @FXML private Button     buttonAddLibrary;
    @FXML private Label      buttonAddIcon;
    @FXML private Label      buttonAddLabel;
    @FXML private Label      libraryLabel;
    @FXML private Label      themeLabel;
    @FXML private Label      themeActiveLabel;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    public static SettingsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        instance = this;
        parentController = UserInterfaceController.getInstance();
        windowFrameController.removeResizeControls();

        libraryLabel.setText(CONST.LIBRARIES);
        buttonAddIcon.setText(CONST.ICON_PLUS);
        buttonAddLabel.setText(CONST.ADD);
        themeLabel.setText(CONST.THEME);
        themeActiveLabel.setText(CONST.ACTIVE);

        comboBoxTheme.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Theme> call(ListView<Theme> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Theme item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getTitle());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        comboBoxTheme.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Theme t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getTitle());
                } else {
                    setText(null);
                }
            }
        });
        comboBoxTheme.getItems().addAll(
                parentController.getThemes()
        );
        comboBoxTheme.getSelectionModel().select(
                parentController.getTheme()
        );
        comboBoxTheme.setOnAction(event -> {
            Theme theme = comboBoxTheme.getSelectionModel().getSelectedItem();
            parentController.setTheme(theme);

            Parent[] parents = new Parent[]{
                    root,
                    UserInterfaceController.getInstance().getRoot()
            };
            for (Parent root : parents) {
                root.getStylesheets().clear();
                root.getStylesheets().add(getClass().getResource(
                        theme.getFilePath()
                ).toExternalForm());
            }
        });

        libraryFolderObservableList = FXCollections.observableArrayList(
                UserInterfaceController.getInstance().getLibraries()
        );
        libraryListView.setCellFactory(new LibraryFolderCellFactory());
        libraryListView.setItems(libraryFolderObservableList);

        buttonAddLibrary.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                addLibrary(selectedDirectory.getAbsolutePath());
            }
        });
    }

    void setStage(Stage stage)
    {
        this.stage = stage;
    }

    void setRoot(Parent parent)
    {
        root = parent;
    }

    WindowFrameController getWindowFrameController()
    {
        return windowFrameController;
    }

    public void removeLibrary(LibraryFolder libraryFolder)
    {
        ConfirmModal confirmDeleteModal = new ConfirmModal(
                ConfirmModal.STYLE_DELETE,
                CONST.LIBRARY_REMOVE,
                CONST.QUESTION_REMOVE[0],
                libraryFolder.getLocation() + " " + CONST.QUESTION_REMOVE[1],
                CONST.REMOVE
        );
        if (confirmDeleteModal.isConfirmed()) {
            UserInterfaceController.getInstance().removeLibrary(libraryFolder, success -> {
                if (success) libraryFolderObservableList.remove(libraryFolder);
            });
        }
    }

    void addLibrary(String path)
    {
        UserInterfaceController.getInstance().addLibrary(path);
        libraryFolderObservableList.add(
                new LibraryFolder(path)
        );
    }
}
