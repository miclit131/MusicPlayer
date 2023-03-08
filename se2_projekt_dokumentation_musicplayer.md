





# SE2-Projekt: Dokumentation





### Authoren

Ermer, Thomas 		te030 

Hellebrand, Patrick 	ph065	

Malliaridis, Christos	cm116





### Projektname

MusicPlayer





### Pfad zum Repository

https://gitlab.mi.hdm-stuttgart.de/te030/MusicPlayer.git

### Java Version

Java JDK 10













## Über das Projekt

###Projektbeschreibung

Das Projekt soll ein klassischer MusicPlayer sein, welcher Audiodateien (momentan nur mp3) aus der Festplatte liest und abspielt. Dabei wird dem Nutzer die Möglichkeit angeboten, eigene Playlists zu erstellen. 

Der MusikPlayer sucht nach Audiodateien nur bei Dateipfaden die der Nutzer festgelegt hat in den Einstellungen, oder, falls kein Dateipfad angegeben, bei dem standard Musikordner von dem jeweiligen Betriebssystem und Account (getestet auf Linux und Windows).

Es werden außerdem die Metadaten der Audiodateien ausgelesen nach I3D-Standards (Version 1 und 2) und entsprechend werden dann die Titel sortiert nach Artisten, Alben und Genre. 

Die von dem Nutzer selbsterstellten Playlisten werden ebenfalls auf der Festplatte gespeichert und können entsprechend bearbeitet oder auch gelöscht werden. Diese werden auch bei dem Neustart der Applikation geladen und angezeigt. 

### Startklasse

Die Startklasse der Software ist die Klasse `App`, die unter `de.hdm_stuttgart.cmpt.App` zufinden ist. 

### Besonderheiten

####FileManager - Implementationen

Es wurden 2 unterschiedliche `FileManager` erstellt um die Implementation von einer Factory zu ermöglichen. Es wurde nur einer vollständig implementiert (`LocalFileManager`) und einer als "Dummy" entworfen (`RemoteFileManager`). 

Der `LocalFileManager` arbeitet lokal, der `RemoteFileManager` sollte eine Verbindung über das Netz erlauben und von externen Rechner Musik abspielen können. Ob das Sinn machen würde ist eine Frage die wir heute noch nicht beantworten konnten. 

#### Multithreading

Der `LocalFileManager` startet mehrere Threads um mit jeden einzelnen Thread jedes Verzeichnis von `MediaLibrary.paths` nach Musik zu scannen. Es kann sein, dass unnötig viele Threads dabei erstellt werden (z.B. bei zu vielen Verzeichnissen in `MediaLibrary.paths`), was eingeschränkt werden kann auf z.B. nur 4 Threads. Ein Threadpool könnte auch verwendet werden um einen saubereren Code zu haben.

#### Ändern und Löschen

Das Löschen von Playlisten oder das Ändern von der MediaLibrary funnktioniert momentan noch ohne Callbacks. Die GUI sagt dem Controller, dass bestimmte Daten gelöscht werden sollen und löscht selber die Elemente die sie selbst geladen hat von der GUI. Falls der Controller erfolgreich war oder nicht, wird aufgrund des fehlenden Callbacks nicht berücksichtigt von der GUI. Eine Verbesserung ist vorgesehen. 

#### Aktualisieren

Geplant ist weiterhin noch das Implementieren der Funktion Aktualisieren zu können während der Laufzeit. Änderungen auf der Festplatte werden während der Laufzeit nicht wahrgenommen. 

#### Fehlende oder fehlerhafte Funktionen

Es gibt bei der Bedienung der GUI noch einige fehlende oder fehlerhafte Funktionen die noch behoben werden müssten. 

## UML Diagramm

Das UML-Diagramm ist als Bild vorhanden im Projekt mit den Namen `UML.png`.



## Stellungnahmen

### Interfaces / Vererbung

Die Interfaces wurden in dem Package `core.interfaces` implementiert. Darunter sind 3 Interfaces zu finden, das `Controller`-Interface, das für die Koordination der kompletten Anwendung zuständig ist, das `FileManager`-Interface, das für das Lesen und Schreiben von Dateien zuständig ist, und das `UserInterface`-Interface, welches die Nutzerschnittstelle ist. 

### Package-Struktur

Die Package-Struktur richtete sich anfangs nach der Struktur vom Beispiel-Projekt von Herrn Kriha. Es gibt zum einen das Package `core` in `de.hdm_stuttgart.cmpt`, welches das wichtigste ist und die komplette Logik und "Beispielimplemenetationen" beinhaltet. Darin befindet sich das Package `logic`, für Klassen die ganzen Objekte des MusicPlayers (z.B. `Song`, `Playlist`), das Package `interfaces`, für alle Interfaces, das Package `implementations`, für die ersten Implementationen und ausführbaren `Controller` und `FIleManager`, das Package `utils`, für alle Utilities die es in Laufe der Zeit benötigt werden, und das Package `exceptions` , für Custom-Exceptions. 

Mit dieser Struktur ist es ebenfalls möglich, in der Ebene des `core`-Package, ein weiteres Package (`implementations`) hinzuzufügen, welches weitere Implementationen von Endnutzern beinhalten könnte. 

### Exceptions

Eine eigene Exception wurde implementiert in `core.exceptions.WrongFileManagerException` welche in `core.implementations.FileManagerFactory` verwendet wird. Diese wird geworfen, falls eine nicht implementierter `FileManager` zurückgegeben werden soll. 

### Grafische Oberfläche (JavaFX)

Die komplette Implementation der GUI ist unter `core.implementations.ui` zu finden.

### Logging

Geloggt wird hauptsächlich nur in kritischen Abläufen. Darunter fallen die Klassen `core.implementations.LocalFileManager`, welcher von der Festplatte Daten / Dateien liest und deshalb sehr fehleranfällig sein kann, und `core.implementations.MainController` welcher alles koordiniert. Die ganzen Threads die erstellt werden in der Zwischenzeit werden ebenfalls geloggt. Es wird außerdem noch die GUI geloggt, was eher unkritischer ist. 

Die Log-Level sind so gewählt, dass sowohl im trace-Level geloggt wird, als auch im warn- und error-Level. Gerade beim `core.implementations.LocalFileManager` kann das sehr gut umgesetzt, werden, wenn zum Beispiel keine Dateien gefunden werden und Default-Objekte verwendet (siehe Fall bei fehlender `core.logic.library.MediaLibrary`-Datei). 

### UML

Einige kleine Änderungen wurden im UML Diagramm gegen Ende nicht berücksichtigt (Stand des UML Diagramms: 8. Juli 2018 ). 

Es wurde außerdem nur bis `UserInterfaceController`-Klasse über die GUI vertieft. Alle in der Hierarchie darunterliegenden Klassen wurden ignoriert. 

### Threads

Threads wurden hauptsächlich für die ganzen Lade-Prozeduren verwendet, die bestimmte Verzeichnisse nach Musik-Dateien suchen und die ganzen `Song`-Objekte erstellen indem die Metadaten ausgelesen werden. Das Hinzufügen der ganzen `Song`-Objekte in einer Liste (`core.logic.library.MediaLibrary.songs`) über die Methode `core.logic.library.MediaLibrary.addSongs(List<Song> songList)` wird parallel über mehrere Threads gemacht (siehe hierzu die Methode `core.implementations.LocalFileManager.loadAllSongsInMultipleThreads(MediaLibrary library)`). 

### Streams und Lambda-Funktionen

`parallelStream` wurde zum Filtern und Sortieren von Playlisten verwendet. 

Siehe dazu in der Klasse `core.implementations.ui.UserInterfaceController` die Methoden `getPlaylists()`, `refreshFavoredPlaylist() ` un in der `initialize`-Methode das `buttonPlay.setOnAction(...);`.

### Dokumentation und Test-Fälle

Zur Dokumentation wurde diese Datei angefertigt. 

Tests wurden für kompliziertere Methoden geschrieben, allerdings auch nicht für alle wichtigen Methoden. Dabei wurden unterschiedliche Parameter ausprobiert und auch erwartete Exceptions bie falscher Eingabe getestet. Siehe dazu das Package `test.java.de.hdm_stuttgart.cmpt`.

### Factories

Es wurde eine Factory nur erstellt und verwendet, unzwar die `implementations.FileManagerFactory.class` zu finden ist. Diese erstellt je nach Parameter (siehe `enum FileManagerGroups` in `logic.interfaces.FileManager`) einen entsprechenden `FileManager`. 