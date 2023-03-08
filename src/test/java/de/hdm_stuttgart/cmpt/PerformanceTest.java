package de.hdm_stuttgart.cmpt;

import de.hdm_stuttgart.cmpt.core.logic.Song;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for performance of hashing calculation
 */
public class PerformanceTest {

    @Test
    public void testHashGenerationSpeed() {
        // Amount of songs
        int i = 10000;
        // Max time in millis
        int max = 20;
        Song smallSong = new Song(
                "This is a test",
                null,
                null,
                0,
                0,
                0,
                10,
                "/This/is/it.mp3");
        Song largeSong = new Song(
                "This is a test song title",
                "Christos as an Artist",
                "The testclass album is this",
                29,
                5,
                2018,
                6131,
                "C:\\Who\\Uses\\Windows\\Nowdays\\For\\Developing\\Applications.mp3");

        long starttime = System.nanoTime();
        for(int x = 0; x < i; x++) {
            smallSong.hashCode();
        }
        double millis = (System.nanoTime() - starttime) / 1000000;
        assertTrue(
                "Calculation of less metadata took more than " + max + " milliseconds.",
                millis < max);

        starttime = System.nanoTime();
        for(int x = 0; x < i; x++) {
            largeSong.hashCode();
        }

        millis = (System.nanoTime() - starttime) / 1000000;
        assertTrue(
                "Calculation of large metadata took more than " + max + " milliseconds.",
                millis < max);
    }


    @Test
    public void testSongListCreation() {
        // Amount of songs
        int i = 10000;
        // Max time in millis
        int max = 10;

        List<Song> songList = new ArrayList<>();
        long starttime = System.nanoTime();
        for(int x = 0; x < i; x++) {
            songList.add(new Song(
                    "This is a test",
                    null,
                    null,
                    0,
                    0,
                    0,
                    10,
                    "/This/is/it.mp3"));
        }
        double millis = (System.nanoTime() - starttime) / 1000000;
        assertTrue(
                "Calculation of less metadata took more than " + max + " milliseconds.",
                millis < max);

        songList = new ArrayList<>();
        starttime = System.nanoTime();
        for(int x = 0; x < i; x++) {
            songList.add(new Song(
                    "This is a test song title",
                    "Christos as an Artist",
                    "The testclass album is this",
                    29,
                    5,
                    2018,
                    6131,
                    "C:\\Who\\Uses\\Windows\\Nowdays\\For\\Developing\\Applications.mp3"));
        }
        millis = (System.nanoTime() - starttime) / 1000000;
        assertTrue(
                "Calculation of large metadata took more than " + max + " milliseconds.",
                millis < max);
    }
}
