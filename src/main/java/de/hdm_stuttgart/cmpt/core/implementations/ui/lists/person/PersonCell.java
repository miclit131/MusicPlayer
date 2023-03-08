package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.person;

import de.hdm_stuttgart.cmpt.core.implementations.ui.CONST;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersonCell extends ListCell<Person> {
    private static Logger log = LogManager.getLogger(PersonCell.class);

    @FXML private HBox personCell;
    @FXML private Label icon;
    @FXML private Label title;
    @FXML private Label position;

    PersonCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/fxml/lists/person-cell.fxml")
            );
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            //log
        }

    }

    @Override
    public void updateItem(Person item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null || !empty) {
            // values
            icon.setText(CONST.ICON_MALE);
            title.setText(item.getTitle());
            position.setText(item.getPosition());

            // finally
            setGraphic(this.personCell);
        } else {
            setGraphic(null);
        }
    }
}
