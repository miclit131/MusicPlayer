package de.hdm_stuttgart.cmpt.core.logic;

import de.hdm_stuttgart.cmpt.core.interfaces.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class Theme {
    private String title;
    private String filePath;

    public Theme(String title, String filePath) {
        this.title = title;
        this.filePath = filePath;
    }

    public Theme(JSONObject object) {
        filePath = object.getString(FileManager.THEME_PATH);
        title = object.getString(FileManager.THEME_NAME);
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put(FileManager.THEME_NAME, title)
                .put(FileManager.THEME_PATH,  filePath)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        else if(o instanceof Theme) return ((Theme) o).getFilePath().equals(this.getFilePath());
        else return false;
    }
}
