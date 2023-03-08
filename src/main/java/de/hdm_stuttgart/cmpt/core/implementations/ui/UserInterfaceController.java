package de.hdm_stuttgart.cmpt.core.implementations.ui;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.StageSettings;
import de.hdm_stuttgart.cmpt.core.implementations.ui.general.ViewManager;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.libraryFolder.LibraryFolder;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.nav.Nav;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.nav.NavCellFactory;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.playlist.PlaylistCellFactory;
import de.hdm_stuttgart.cmpt.core.implementations.ui.lists.song.SongCellFactory;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.about.About;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.loader.InitLoader;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.ConfirmModal;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InfoModal;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.modal.InputModal;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.playlistEdit.PlaylistEdit;
import de.hdm_stuttgart.cmpt.core.implementations.ui.stages.settings.Settings;
import de.hdm_stuttgart.cmpt.core.interfaces.Controller;
import de.hdm_stuttgart.cmpt.core.interfaces.UserInterface;
import de.hdm_stuttgart.cmpt.core.logic.Player;
import de.hdm_stuttgart.cmpt.core.logic.Song;
import de.hdm_stuttgart.cmpt.core.logic.Theme;
import de.hdm_stuttgart.cmpt.core.logic.playlist.CurrentPlaylist;
import de.hdm_stuttgart.cmpt.core.logic.playlist.Playlist;
import de.hdm_stuttgart.cmpt.core.utils.Converter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static de.hdm_stuttgart.cmpt.core.implementations.ui.CONST.*;
import static java.util.stream.Collectors.toList;

public class UserInterfaceController extends Application implements UserInterface, Initializable {

    private static Logger log = LogManager.getLogger(UserInterfaceController.class);

    private static UserInterfaceController uiController;
    private Parent root;

    // Player
    private double elapsed = 0;
    private Controller mainController;
    private Stage stage;
    private Parent parent;
    private Timeline timeline;
    // Playlist
    private Playlist activePlaylist;
    private int listLastSortedBy;
    private int activeView;
    // data
    private ObservableList<Playlist> playlistObservableList;
    private ObservableList<Song> songObservableList;
    private Playlist favoredPlaylist;

    private ToggleGroup sortingGroup;
    private RadioButton buttonSortAlphabetical;
    private RadioButton buttonSortCreatedAt;
    private RadioButton buttonSortLastHeard;
    private RadioButton buttonSortArtist;
    private RadioButton buttonSortYear;
    private RadioButton buttonSortAlbum;

    private ListView<Playlist> playlistListView;
    private ListView<Song> songListView;

    @FXML private ListView<Nav> navListView;
    @FXML private ListView<Nav> navBottomListView;
    @FXML private BorderPane borderPaneCenter;
    @FXML private BorderPane viewHeader;
    @FXML private Label labelLibrary;
    @FXML private Label listTitleActive;
    @FXML private HBox groupSortingButtons;
    // ListView
    @FXML private HBox hBoxSongListOptions;
    @FXML private HBox hBoxOptionalListViewButtons;
    @FXML private Button buttonPlaySongList;
    @FXML private Label  iconPlaySongList;
    @FXML private Label  labelPlaySongList;
    @FXML private Button buttonShuffleSongList;
    @FXML private Label  iconShuffleSongList;
    @FXML private Label  labelShuffleSongList;
    // Player
    @FXML private Label  labelSongAlbumPlaceholder;
    @FXML private Label  labelActiveSongTitle;
    @FXML private Label  labelActiveSongInfo;
    @FXML private Button buttonShuffle;
    @FXML private Button buttonPrev;
    @FXML private Button buttonPlay;
    @FXML private Button buttonNext;
    @FXML private Button buttonRepeat;
    @FXML private Label  labelActiveSongTimeElapsed;
    @FXML private Label  labelActiveSongLength;
    @FXML private Label  labelRepeat;
    @FXML private ProgressBar progressBarPlayer;
    @FXML private Slider      sliderPlayer;

    public static UserInterfaceController getInstance() {
        if(uiController == null) {
            log.trace("Launch user interface...");
            launch();
        }
        return uiController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.trace("Start initialization of user interface controller...");
        mainController = MainController.getInstance();
        // init CONST
        labelLibrary.setText(CONST.LIBRARY);
        buttonSortAlphabetical = new RadioButton(CONST.ALPHABETICAL);
        buttonSortLastHeard = new RadioButton(CONST.LAST_HEARD);
        buttonSortYear = new RadioButton(CONST.YEAR);
        buttonSortAlbum = new RadioButton(CONST.ALBUM);
        buttonSortCreatedAt = new RadioButton(CONST.CREATION_DATE);
        buttonSortArtist = new RadioButton(CONST.ARTIST);
        // ListView
        iconPlaySongList.setText(CONST.ICON_PLAY);
        labelPlaySongList.setText(CONST.PLAY);
        iconShuffleSongList.setText(CONST.ICON_SHUFFLE);
        labelShuffleSongList.setText(CONST.SHUFFLE);
        // Player
        buttonShuffle.setText(CONST.ICON_SHUFFLE);
        buttonPrev.setText(CONST.ICON_STEP_PREV);
        updatePlayButton();
        buttonNext.setText(CONST.ICON_STEP_NEXT);
        buttonRepeat.setText(CONST.ICON_REPEAT);
        updateRepeatButton();
        labelActiveSongTitle.setText(CONST.SONG_NONE);
        labelActiveSongInfo.setText(CONST.ACTIVE);
        labelSongAlbumPlaceholder.setText(CONST.ICON_SONG);
        labelActiveSongTimeElapsed.setText("-");
        labelActiveSongLength.setText("-:--");
        updateCurrentSongInfo();

        sortingGroup = new ToggleGroup();

        progressBarPlayer.setProgress(0);
        sliderPlayer.setMax(1);
        sliderPlayer.setMin(0);
        sliderPlayer.setValue(0);
        sliderPlayer.setDisable(true);

        playlistListView = new ListView<>();
        playlistListView.setCellFactory(new PlaylistCellFactory());
        songListView = new ListView<>();
        songListView.setCellFactory(new SongCellFactory());

        log.debug("creating nav items");
        navListView.setCellFactory(new NavCellFactory());
        navListView.getItems().addAll(
                new Nav(Nav.PLAYLISTS, CONST.PLAYLISTS, CONST.ICON_PLAYLIST),
                new Nav(Nav.FAVORITE, CONST.FAVORITE, CONST.ICON_FAVORITE),
                new Nav(Nav.SONGS, CONST.SONGS_ALL, CONST.ICON_SONG),
                new Nav(Nav.ALBUMS, CONST.ALBUMS, CONST.ICON_DISK),
                new Nav(Nav.ARTISTS, CONST.ARTISTS, CONST.ICON_ARTIST),
                new Nav(Nav.GENRES, CONST.GENRES, CONST.ICON_GENRE),
                new Nav(Nav.PLAYING, CONST.PLAYING_NOW, CONST.ICON_VISUAL)
        );
        log.debug("creating bottom nav item");
        navBottomListView.setCellFactory(new NavCellFactory());
        navBottomListView.getItems().addAll(
                new Nav(Nav.ABOUT, CONST.ABOUT, CONST.ICON_ABOUT),
                new Nav(Nav.SETTINGS, CONST.SETTINGS, CONST.ICON_SETTINGS)
        );
        log.debug("loading initial view");
        activeView = VIEW_PLAYLISTS;
        clearView();
        loadPlaylistsView();
        navListView.getSelectionModel().select(Nav.PLAYLISTS);

        // events
        navListView.setOnMouseClicked(mouseEvent -> {
            clearView();
            switch (navListView.getSelectionModel().getSelectedItem().getId()) {
                case Nav.PLAYLISTS:
                    log.debug("clicked VIEW_PLAYLISTS");
                    activeView = VIEW_PLAYLISTS;
                    loadPlaylistsView();
                    break;
                case Nav.SONGS:
                    log.debug("clicked VIEW_SONGS_ALL");
                    activeView = VIEW_SONGS_ALL;
                    loadSongsView(CONST.SONGS_ALL, getSongs());
                    break;
                case Nav.FAVORITE:
                    log.debug("clicked VIEW_SONGS_FAVORED");
                    activeView = VIEW_SONGS_FAVORED;
                    loadSongsView(
                            CONST.FAVORITE,
                            getFavoredPlaylist().getSongs()
                    );
                    break;
                case Nav.ALBUMS:
                    log.debug("clicked VIEW_ALBUMS");
                    activeView = VIEW_ALBUMS;
                    loadAlbumsView();
                    break;
                case Nav.ARTISTS:
                    log.debug("clicked VIEW_ARTISTS");
                    activeView = VIEW_ARTISTS;
                    loadArtistsView();
                    break;
                case Nav.GENRES:
                    log.debug("clicked VIEW_GENRES");
                    activeView = VIEW_GENRES;
                    loadGenresView();
                    break;
                case Nav.PLAYING:
                    log.debug("clicked VIEW_PLAYING");
                    activeView = VIEW_PLAYING;
                    Playlist currentPlaylist = mainController.getPlayer().getCurrentPlaylist();
                    if (currentPlaylist != null) {
                        log.info("currentPlaylist name: " + currentPlaylist.getName());
                        loadSongsView(
                                CONST.PLAYING_NOW + ": '" + currentPlaylist.getName() + "'",
                                currentPlaylist.getSongs()
                        );
                    } else {
                        loadSongsView(
                                CONST.PLAYING_NONE,
                                new ArrayList<>()
                        );
                    }
            }
            log.info("activeView is: " + activeView);
        });

        navBottomListView.setOnMouseClicked(mouseEvent -> {
            switch (navBottomListView.getSelectionModel().getSelectedItem().getId()) {
                case Nav.ABOUT:
                    log.debug("creating Stage About");
                    new About();
                    break;
                case Nav.SETTINGS:
                    log.debug("creating Stage Settings");
                    new Settings();
                    break;
            }
        });

        playlistListView.setOnMouseClicked(event -> {
            log.debug("clicked Playlist");
            Playlist clickedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
            if (clickedPlaylist != null) {
                clearView();
                activeView = VIEW_PLAYLIST_SONGS;
                loadSongsView(clickedPlaylist);
            }
        });

        // song listview events
        buttonPlaySongList.setOnAction(event -> {
            playSong(songObservableList.get(0));
        });

        buttonShuffleSongList.setOnAction(event -> {
            playSong(songObservableList.get(0));
            mainController.getPlayer().setShuffle(true);
            updateShuffleButton();
        });

        // player events

        buttonPlay.setOnAction(event -> {
            Player player = mainController.getPlayer();
            switch (player.getStatus()) {
                case Player.STATUS_PLAYING:
                    log.trace("Pause playback");
                    player.pause();
                    timeline.pause();
                    break;
                case Player.STATUS_PAUSED:
                    log.trace("Continue playback");
                    player.play();
                    timeline.play();
                    break;
                case Player.STATUS_STOPPED:
                    log.trace("Starting playback");
                    List<Song> allSongs = getSongs()
                            .parallelStream()
                            .sorted(Song::compareToByTitle)
                            .collect(toList());

                    log.trace("allSongs lenght is " + allSongs.size());
                    Playlist playlistAllSongs = new Playlist(
                            CONST.SONGS_ALL,
                            allSongs
                    );
                    player.setCurrentPlaylist(new CurrentPlaylist(playlistAllSongs));
                    player.setCurrentSong(allSongs.get(0));
                    songListView.refresh();
                    updateCurrentSongInfo();
            }
            updatePlayButton();
        });

        buttonNext.setOnAction(event -> {
            // TODO IMPORTANT Update UI when song changed
            mainController.getPlayer().next();
        });
        buttonPrev.setOnAction(event -> {
            // TODO IMPORTANT Update UI when song changed
            mainController.getPlayer().previous();
        });

        buttonShuffle.setOnAction(event -> {
            Player player = mainController.getPlayer();
            player.setShuffle(!player.isShuffle());
            updateShuffleButton();
        });

        buttonRepeat.setOnAction(event -> {
            Player player = mainController.getPlayer();
            int repeatMode = player.getRepeatMode();
            if (repeatMode < (Player.REPEAT_MODES.length - 1)) repeatMode++;
            else repeatMode = Player.MODE_REPEAT_NONE;
            player.setRepeatMode(repeatMode);
            updateRepeatButton();
        });

        log.trace("UserInterface initialized");
    }

    public Stage getStage() {
        return stage;
    }

    public Parent getRoot() {
        return parent;
    }

    private void setStageValues(Stage stage, Parent parent) {
        this.stage = stage;
        this.parent = parent;
    }

    public void showInfoModal(int INFO_MODAL_STYLE, String message, String text) {
        new InfoModal(INFO_MODAL_STYLE, message, text);
    }

    private void clearView() {
        log.trace("clearing center view");
        // clear buttons
        viewHeader.setLeft(null);
        hBoxSongListOptions.setVisible(false);
        hBoxOptionalListViewButtons.getChildren().clear();
        // toggleGroup clear
        groupSortingButtons.getChildren().clear();
        // remove listView
        borderPaneCenter.setCenter(null);
    }

    private void loadSongsView(String title, List<Song> songs) {
        log.debug("creating songs view for: " + title);
        // set view title
        listTitleActive.setText(title);
        hBoxSongListOptions.setVisible(true);
        // set sort buttons
        ViewManager.setSortButtons(
                groupSortingButtons,
                new RadioButton[]{
                        buttonSortAlphabetical,
                        buttonSortArtist,
                        buttonSortAlbum,
                        buttonSortYear
                }
        );
        // view new list
        log.trace("Length of list is " + songs.size());
        songObservableList = FXCollections.observableList(songs);
        songListView.setItems(songObservableList);
        borderPaneCenter.setCenter(songListView);
        // sort new list
        sortSongsCollection(Song.SORT_TITLE, songObservableList);
        // events
        buttonSortAlphabetical.setOnAction(event -> sortSongsCollection(Song.SORT_TITLE, songObservableList));
        buttonSortArtist.setOnAction(event -> sortSongsCollection(Song.SORT_ARTIST, songObservableList));
        buttonSortAlbum.setOnAction(event -> sortSongsCollection(Song.SORT_ALBUM, songObservableList));
        buttonSortYear.setOnAction(event -> sortSongsCollection(Song.SORT_YEAR, songObservableList));
    }

    private void loadSongsView(Playlist playlist) {
        activePlaylist = playlist;
        log.debug("loading songs view for playlist: " + playlist.getName());
        // create back button
        Button buttonBack = ViewManager.createBackButton();
        viewHeader.setLeft(buttonBack);
        // create buttons
        Button buttonEdit = ViewManager.createIconButton(CONST.ICON_EDIT);
        Button buttonDelete = ViewManager.createIconButton(CONST.ICON_DELETE);
        hBoxOptionalListViewButtons.getChildren().addAll(
                buttonEdit,
                buttonDelete
        );

        loadSongsView(playlist.getName(), playlist.getSongs());

        // events
        buttonBack.setOnAction(event -> {
            clearView();
            loadPlaylistsView();
        });

        buttonEdit.setOnAction(event -> {
            log.debug("Opening playlist edit window");
            new PlaylistEdit(activePlaylist.getName(), activePlaylist.getSongs());
        });

        buttonDelete.setOnAction(event -> {
            deletePlaylist(activePlaylist);
        });
    }

    private void loadPlaylistsView() {
        log.debug("loading playlists view");
        // set view title
        listTitleActive.setText(CONST.PLAYLISTS);
        // create newPlaylist button
        Button buttonPlaylistNew = ViewManager.createButton(CONST.ICON_NEW, CONST.PLAYLIST_NEW);
        hBoxOptionalListViewButtons.getChildren().add(buttonPlaylistNew);
        // set sort buttons
        ViewManager.setSortButtons(
                groupSortingButtons,
                new RadioButton[]{
                        buttonSortAlphabetical,
                        buttonSortLastHeard,
                        buttonSortCreatedAt
                }
        );
        // view new list
        borderPaneCenter.setCenter(playlistListView);
        playlistObservableList = FXCollections.observableList(getPlaylists());
        playlistListView.setItems(playlistObservableList);
        // sort new list
        sortPlaylistsCollection(Playlist.SORT_NAME);
        // events
        buttonSortAlphabetical.setOnAction(event -> sortPlaylistsCollection(Playlist.SORT_NAME));
        buttonSortLastHeard.setOnAction(event -> sortPlaylistsCollection(Playlist.SORT_LAST_HEARD));
        buttonSortCreatedAt.setOnAction(event -> sortPlaylistsCollection(Playlist.SORT_CREATED_AT));
        buttonPlaylistNew.setOnAction(event -> {
            InputModal playlistNew = new InputModal(CONST.PLAYLIST_NEW, CONST.CREATE, CONST.ICON_PLAYLIST);
            log.debug("New playlist stage closed");
            if (playlistNew.isSet()) {
                log.debug("Creating new playlist: " + playlistNew.getText());
                createPlaylist(playlistNew.getText());
            } else {
                log.debug("No playlist was created");
            }
        });
    }

    private void loadAlbumsView() {
        log.debug("loading albums view");
        // set view title
        listTitleActive.setText(CONST.ALBUMS);
    }

    private void loadArtistsView() {
        log.debug("loading artists view");
        // set view title
        listTitleActive.setText(CONST.ARTISTS);
    }

    private void loadGenresView() {
        log.debug("loading genres view");
        // set view title
        listTitleActive.setText(CONST.GENRES);
    }

    private void sortSongsCollection(int key, ObservableList<Song> songCollection) {
        listLastSortedBy = key;
        switch (key) {
            case Song.SORT_TITLE:
                log.debug("sorting songs: title");
                FXCollections.sort(songCollection, Song.comparatorSortTitle);
                break;
            case Song.SORT_ARTIST:
                log.debug("sorting songs: artist");
                FXCollections.sort(songCollection, Song.comparatorSortArtist);
                break;
            case Song.SORT_ALBUM:
                log.debug("sorting songs: album");
                FXCollections.sort(songCollection, Song.comparatorSortAlbum);
                break;
            case Song.SORT_YEAR:
                log.debug("sorting songs: year");
                FXCollections.sort(songCollection, Song.comparatorSortYear);
                break;
        }
    }

    private void sortPlaylistsCollection(int key) {
        listLastSortedBy = key;
        switch (key) {
            case Playlist.SORT_NAME:
                log.debug("sorting playlists: name");
                FXCollections.sort(playlistObservableList, Playlist.comparatorSortName);
                break;
            case Playlist.SORT_LAST_HEARD:
                log.debug("sorting playlists: lastHeard");
                FXCollections.sort(playlistObservableList, Playlist.comparatorSortLastHeard);
                FXCollections.reverse(playlistObservableList);
                break;
            case Playlist.SORT_CREATED_AT:
                log.debug("sorting playlists: createdAt");
                FXCollections.sort(playlistObservableList, Playlist.comparatorSortCreatedAt);
                FXCollections.reverse(playlistObservableList);
                break;
        }
    }

    // PLAYLISTS

    private List<Playlist> getPlaylists() {
        return mainController
                .getPlaylists()
                .parallelStream()
                .filter(playlist -> playlist.getType() == Playlist.TYPE_NORMAL)
                .collect(toList());
    }

    private void createPlaylist(String title) {
        log.debug("creating playlist: " + title);
        Playlist newPlaylist = new Playlist(
                title,
                new ArrayList<>()
        );
        mainController.createPlaylist(newPlaylist);
        sortPlaylistsCollection(listLastSortedBy);
    }

    private void deletePlaylist(Playlist playlist) {
        ConfirmModal deleteModal = new ConfirmModal(
                ConfirmModal.STYLE_DELETE,
                CONST.PLAYLIST_DELETE,
                CONST.QUESTION_DELETE[0],
                "'" + playlist.getName() + "' " + CONST.QUESTION_DELETE[1],
                CONST.DELETE
        );
        if (deleteModal.isConfirmed()) {
            log.debug("Removing playlist");
            mainController.deletePlaylist(playlist);
            clearView();
            loadPlaylistsView();
        }
    }

    public void updateActivePlaylist(Playlist newPlaylist) {
        log.debug("updating activePlaylist");
        mainController.updatePlaylist(
                activePlaylist,
                newPlaylist
        );
        activePlaylist = newPlaylist;
        listTitleActive.setText(newPlaylist.getName());
        songObservableList.clear();
        songObservableList.addAll(newPlaylist.getSongs());
        sortSongsCollection(listLastSortedBy, songObservableList);
    }

    // SONGS
    public void playSong(Song startSong) {
        log.info("Playing: " + startSong.getTitle());
        log.debug("AutoPlaylist discovery on");
        Player player = mainController.getPlayer();
        switch (activeView) {
            case VIEW_PLAYLIST_SONGS:
                player.setCurrentPlaylist(new CurrentPlaylist(activePlaylist));
                break;
            case VIEW_SONGS_FAVORED:
                player.setCurrentPlaylist(new CurrentPlaylist(getFavoredPlaylist()));
                break;
            case VIEW_SONGS_ALL:
                player.setCurrentPlaylist(new CurrentPlaylist(new Playlist(CONST.SONGS_ALL,songObservableList)));
                break;
        }
        player.setCurrentSong(startSong);
        songListView.refresh();
        updateCurrentSongInfo();
    }

    public void updateCurrentSongInfo() {
        Player player = mainController.getPlayer();
        Song currentSong = player.getCurrentSong();
        if (currentSong != null) {
            if (timeline != null) timeline.stop();
            sliderPlayer.setValue(0);
            labelActiveSongTitle.setText(currentSong.getTitle());
            labelActiveSongInfo.setText(currentSong.getArtist() + " • " + currentSong.getAlbum());
            labelActiveSongLength.setText(Converter.secondsToString(currentSong.getLength()));
            elapsed = 0;
            sliderPlayer.setDisable(false);
            player.play();
            updatePlayButton();

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(500),
                    ae -> {
                        elapsed = player.getTime();
                        labelActiveSongTimeElapsed.setText(Converter.secondsToString(elapsed));
                        double position = player.getTime() / currentSong.getLength();
                        progressBarPlayer.setProgress(position);
                        sliderPlayer.setValue(position);
                    }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            sliderPlayer.setOnMouseReleased((value) -> {
                double v = sliderPlayer.getValue();
                player.setTime(v * currentSong.getLength());
                progressBarPlayer.setProgress(v);
            });
        }
    }

    public List<Song> getSongs() {
        return mainController.getMediaLibrary().getSongs();
    }

    private void refreshFavoredPlaylist() {
        Optional<Playlist> pl = mainController
                .getPlaylists()
                .parallelStream()
                .filter(playlist -> playlist.getType() == Playlist.TYPE_FAVORED)
                .findFirst();
        Playlist s;
        if(!pl.isPresent()) {
            log.warn("No favored playlist found.");
            favoredPlaylist = new Playlist(Playlist.TYPE_FAVORED, CONST.FAVORITE, new ArrayList<>());
            mainController.createPlaylist(favoredPlaylist);
        } else {
            log.warn("Favored playlist found.");
            favoredPlaylist = pl.get();
        }
    }

    public Playlist getFavoredPlaylist() {
        if (favoredPlaylist == null) refreshFavoredPlaylist();
        return favoredPlaylist;
    }

    public void removeSongFromFavored(Song song) {
        Playlist playlist = getFavoredPlaylist();
        List<Song> playlistSongs = playlist.getSongs();
        playlistSongs.remove(song);
        mainController.updatePlaylist(
                playlist,
                new Playlist(
                        Playlist.TYPE_FAVORED,
                        playlist.getName(),
                        playlistSongs
                )
        );
        if (activeView == VIEW_SONGS_FAVORED) songObservableList.remove(song);
        refreshFavoredPlaylist();
    }

    public void addSongToFavored(Song song) {
        log.trace("Add song to favored...");
        Playlist playlist = getFavoredPlaylist();
        List<Song> playlistSongs = playlist.getSongs();
        playlistSongs.add(song);
        mainController.updatePlaylist(
                playlist,
                new Playlist(
                        Playlist.TYPE_FAVORED,
                        playlist.getName(),
                        playlistSongs
                )
        );
        refreshFavoredPlaylist();
    }

    // LIBRARIES

    public List<LibraryFolder> getLibraries() {
        List<String> libraries = mainController.getMediaLibrary().getPaths();
        List<LibraryFolder> libraryFolderArrayList = new ArrayList<>();
        for (String library : libraries) {
            libraryFolderArrayList.add(new LibraryFolder(library));
        }
        return libraryFolderArrayList;
    }

    public void addLibrary(String path) {
        mainController.addLibraryPath(path);
    }

    public void removeLibrary(LibraryFolder libraryFolder, Consumer<Boolean> removeFromListFunction) {
        mainController.removeLibraryPath(libraryFolder.getLocation());
        removeFromListFunction.accept(true);
    }

    // PLAYER

    private void updateRepeatButton() {
        buttonRepeat.getStyleClass().remove("active");
        labelRepeat.getStyleClass().remove("inactive");
        switch (mainController.getPlayer().getRepeatMode()) {
            case Player.MODE_REPEAT_NONE:
                log.trace("repeat mode set to: repeat none");
                labelRepeat.setText(CONST.NONE);
                labelRepeat.getStyleClass().add("inactive");
                break;
            case Player.MODE_REPEAT_ALL:
                log.trace("repeat mode set to: repeat all");
                labelRepeat.setText(CONST.ALL);
                buttonRepeat.getStyleClass().add("active");
                break;
            case Player.MODE_REPEAT_ONE:
                log.trace("repeat mode set to: repeat one");
                labelRepeat.setText(CONST.ONE);
                buttonRepeat.getStyleClass().add("active");
                break;
        }
    }

    private void updatePlayButton() {
        switch (mainController.getPlayer().getStatus()) {
            case Player.STATUS_PLAYING:
                buttonPlay.setText(CONST.ICON_PAUSE);
                break;
            default:
                buttonPlay.setText(CONST.ICON_PLAY);
                break;
        }
    }

    private void updateShuffleButton() {
        Player player = mainController.getPlayer();
        if (player.isShuffle()) buttonShuffle.getStyleClass().add("active");
        else buttonShuffle.getStyleClass().remove("active");
    }

    public Song getCurrentSong() {
        return mainController.getPlayer().getCurrentSong();
    }

    public static Theme getTheme() {
        return MainController.getInstance().getTheme();
    }

    public void setTheme(Theme theme) {
        mainController.setTheme(theme);
    }

    public Theme[] getThemes() {
        return mainController.getThemes();
    }

    // GUI

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.trace("Start initialization of user interface...");
        mainController = MainController.getInstance();
        // TODO Check if a Controller is instantiated before creating GUI (to prevent running headless GUI)
        log.debug("Starting loading screen...");
        InitLoader loadingStage = new InitLoader();

        Platform.runLater(() -> {
            log.debug("Starting UserInterface...");
            FXMLLoader loader = new FXMLLoader();
            try {
                loader.setLocation(getClass().getResource("/fxml/app.fxml"));
                root = loader.load();
                log.info("FXML loaded.");
            } catch (Exception e) {
                log.fatal("Main Stage could not be loaded:");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.fatal(sw.toString());
                loadingStage.close();
                new InfoModal(
                        InfoModal.STYLE_ERROR,
                        CONST.ERROR_CRITICAL,
                        e.getLocalizedMessage() + "\n" + CONST.COULD_NOT_START
                );
            }
            log.trace("Set user interface controller...");
            uiController = loader.getController();
            log.trace("User interface controller set to " + uiController);
            uiController.setStageValues(primaryStage, root);

            log.debug("Adding stylesheet");
            root.getStylesheets().add(getClass().getResource(
                    getTheme().getFilePath()
            ).toExternalForm());

            primaryStage.setTitle(CONST.APP_NAME);
            primaryStage.setWidth(CONST.MAIN_WINDOW_WIDTH);
            primaryStage.setHeight(CONST.MAIN_WINDOW_HEIGHT);

            Platform.runLater(() -> {
                log.debug("Showing main stage");
                StageSettings.defaultShow(root, primaryStage);
                log.debug("Closing loading stage");
                loadingStage.close();
            });
        });
    }
}
