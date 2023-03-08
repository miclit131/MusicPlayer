package de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder;

import java.util.ArrayList;

public class LibraryFolder extends ArrayList<LibraryFolder> {
    private String location;

    public LibraryFolder(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public boolean equals(Object o) {
        if (o instanceof LibraryFolder) {
            LibraryFolder that = (LibraryFolder) o;
            return this.getLocation().equals(that.getLocation());
        }
        return false;
    }
}
