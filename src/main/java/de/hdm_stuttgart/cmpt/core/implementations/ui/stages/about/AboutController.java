package de.hdm_stuttgart.cmpt.core.implementations.ui.stages.about;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.WindowFrameController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.person.Person;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.person.PersonCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    private Stage stage;

    @FXML private BorderPane about;
    @FXML private Label appName;
    @FXML private Label version;
    @FXML private Label description;
    @FXML private Hyperlink gitlabHyperlink;
    @FXML private Hyperlink felgromHyperlink;
    @FXML private ListView<Person> personListView;

    @FXML private Parent windowFrame;
    @FXML private WindowFrameController windowFrameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appName.setText(CONST.APP_NAME);
        version.setText(CONST.APP_VERSION);
        description.setText(CONST.APP_DESC);
        gitlabHyperlink.setText(CONST.GITLAB);
        felgromHyperlink.setText(CONST.FELGROM);

        personListView.setCellFactory(new PersonCellFactory());
        personListView.getItems().addAll(
                new Person("Christos Malliaridis", "Development & App Architecture"),
                new Person("Patrick Hellebrand", "Design & JavaFX Dev"),
                new Person("Thomas Ermer", "Doc commenter")
        );
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    WindowFrameController getWindowFrameController() {
        return windowFrameController;
    }
}
