package de.hdm_stuttgart.cmpt.core.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class Converter {

    private static final String PLAYLIST_NAME = "name";
    private static final String PLAYLIST_SONGS = "list";
    private static final String LIBRARY_NAME = PLAYLIST_NAME;
    private static final String MEDIALIBRARY_SONGS = PLAYLIST_SONGS;

    public final static String CHARSET = "UTF-8";

    public static JSONArray filesToJSONArray(List<File> files) {
        JSONArray jsonArray = new JSONArray();
        for (File file : files) {
            jsonArray.put(fileToJSONObject(file));
        }
        return jsonArray;
    }

    public static JSONArray fileToJSONArray(File file) {
        return new JSONArray(file);
    }

    public static JSONObject fileToJSONObject(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            byte[] bytes = stream.readAllBytes();
            String content = new String(bytes);
            return new JSONObject(content);
        } catch (IOException e) {
            // TODO Handle exception
        }
        return null;
    }

    public static String secondsToString(double totalSeconds) {
        totalSeconds /= 1000;
        long min = (long) totalSeconds / 60;
        long sec = (long) totalSeconds % 60;
        return String.format("%d:%02d", min, sec);
    }

}
