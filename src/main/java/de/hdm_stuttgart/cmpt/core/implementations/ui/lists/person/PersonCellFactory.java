package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.person;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PersonCellFactory implements Callback<ListView<Person>, ListCell<Person>> {
    @Override
    public ListCell<Person> call(ListView<Person> listview) {
        return new PersonCell();
    }
}