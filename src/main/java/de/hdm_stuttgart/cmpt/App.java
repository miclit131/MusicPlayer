package de.hdm_stuttgart.cmpt;

import de.hdm_stuttgart.cmpt.core.implementations.MainController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static Logger log = LogManager.getLogger(App.class);

    /**
     * Your application's main entry point.
     *
     * @param args Yet unused
     */
    public static void main( String[] args ) {

        log.debug("Create controller...");
        MainController mainController = new MainController();
        log.debug("Controller created.");
        log.debug("Starting controller...");
        mainController.start();
    }
}
