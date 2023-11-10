package main.boundary;

import main.boundary.welcome.Welcome;
import main.controller.account.AccountManager;
import main.controller.camp.CampManager;

/**
 * This class is the entry point of the application.
 */
public class UIEntry {
    /**
     * Checks if the application is being run for the first time.
     *
     * @return true if the application is being run for the first time, false otherwise.
     */
    private static boolean firstStart() {
        return AccountManager.repositoryIsEmpty() && CampManager.repositoryIsEmpty();
        //return true;
    }

    /**
     * Starts the application.
     * If the application is being run for the first time, it loads the default users and projects.
     * Then it displays the welcome page.
     */
    public static void start() {
        if (firstStart()) {
            AccountManager.loadUsers();
            //CampManager.loadCamps();
        }
        Welcome.welcome();
    }
}