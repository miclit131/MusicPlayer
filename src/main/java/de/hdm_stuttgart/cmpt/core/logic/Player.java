package de.hdm_stuttgart.cmpt.core.logic;

import de.hdm_stuttgart.cmpt.core.logic.playlist.CurrentPlaylist;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Player {

    private static Logger log = LogManager.getLogger(Player.class);

    private MediaPlayer mediaPlayer;

    public static final int STATUS_PLAYING   = 0;
    public static final int STATUS_PAUSED    = 1;
    public static final int STATUS_STOPPED   = 2;

    private static final int FIRST_SONG_INDEX = 0;

    public static final int MODE_REPEAT_NONE = 0;
    public static final int MODE_REPEAT_ALL  = 1;
    public static final int MODE_REPEAT_ONE  = 2;
    public static final int[] REPEAT_MODES = {
            MODE_REPEAT_NONE,
            MODE_REPEAT_ALL,
            MODE_REPEAT_ONE
    };

    private boolean shuffle;
    private int     repeatMode;
    private int     status;
    private CurrentPlaylist currentPlaylist;
    private int  currentSongIndex;
    private Song currentSong;

    public Player() {
        this.shuffle = false;
        this.repeatMode = MODE_REPEAT_NONE;
        this.status = STATUS_STOPPED;
        this.currentSongIndex = 0;

        // TODO See if this constructor makes any sense (constructor without a playlist).
        // Probably does, because we have to preload (create an instance of Player)somehow without knowing what the user wants to play yet.
        // We can just use setCurrentPlaylist later on
    }

    public Player(Playlist playlist) {
        shuffle = false;
        repeatMode = MODE_REPEAT_NONE;
        status = STATUS_STOPPED;
        currentSongIndex = 0;
        currentPlaylist = new CurrentPlaylist(playlist);
        currentSong = currentPlaylist.getSong(FIRST_SONG_INDEX, shuffle);
        mediaPlayer = new MediaPlayer(new Media(new File(currentSong.getFilePath()).toURI().toString()));
    }

    public void play() {
        if(status != STATUS_PAUSED) {
            Media hit = new Media(new File(currentSong.getFilePath()).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
        }
        status = STATUS_PLAYING;
        mediaPlayer.play();
        //TODO implement a method to play the currentSong and change status to STATUS_PLAYING.
    }

    public void stop() {
        status = STATUS_STOPPED;
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
        //TODO implement a method to stop the currentSong and change status to STATUS_STOPPED.
    }

    public void pause() {
        status = STATUS_PAUSED;
        if(mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
        //TODO implement a method to pause the currentSong and change status to STATUS_PAUSED.
    }

    public void next() {
        if (currentSongIndex == currentPlaylist.getNumberOfSongs()) {
            this.currentSong = this.currentPlaylist.getSong(FIRST_SONG_INDEX, this.shuffle);
        } else {
            this.currentSong = this.currentPlaylist.getSong(this.currentSongIndex + 1, this.shuffle);
            this.currentSongIndex += 1;
        }
        if (status == STATUS_PLAYING) {
            stop();
        }
        play();
    }

    public void previous() {
        if (currentSongIndex == currentPlaylist.getNumberOfSongs()) {
            this.currentSong = this.currentPlaylist.getSong(FIRST_SONG_INDEX, this.shuffle);
        } else {
            this.currentSong = this.currentPlaylist.getSong(this.currentSongIndex - 1, this.shuffle);
            this.currentSongIndex -= 1;
        }
        if (status == STATUS_PLAYING) {
            stop();
        }
        play();
    }

    public void setCurrentSong(Song song) {
        stop();
        currentSong = currentPlaylist.getSong(currentPlaylist.orderedlist.indexOf(song), false);
    }

    public void setCurrentSong(int index) {
        currentSong = this.currentPlaylist.getSong(index, this.shuffle);
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentPlaylist(Playlist playlist) {
        this.currentPlaylist = (CurrentPlaylist) playlist;
        this.currentSong = this.currentPlaylist.getSong(FIRST_SONG_INDEX, this.shuffle);
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        if (shuffle) {
            this.currentPlaylist.randomizePlaylist();
        }
    }

    public boolean isShuffle()
    {
        return shuffle;
    }

    public void setRepeatMode(int repeatMode)
    {
        this.repeatMode = repeatMode;
    }

    public int getRepeatMode()
    {
        return repeatMode;
    }

    public int getStatus() {
        return status;
    }

    /**
     * Sets the volume state from 0.0 to 1.0
     * @param volume volume value
     */
    public void setVolume(double volume) {
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void mute(boolean mute) {
        if(mediaPlayer != null) {
            mediaPlayer.setMute(mute);
        }
    }

    public double getTime() {
        return mediaPlayer.getCurrentTime().toMillis();
    }

    public void setTime(double millis) {
        mediaPlayer.seek(Duration.millis(millis));
    }
}
