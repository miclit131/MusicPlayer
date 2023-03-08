package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.person;

import java.util.ArrayList;

public class Person extends ArrayList<Person> {
    private String title;
    private String position;

    public Person(String title, String position) {
        this.title = title;
        this.position = position;
    }

    String getTitle() {
        return title;
    }

    String getPosition() {
        return position;
    }
}
