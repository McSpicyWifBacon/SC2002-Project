/**
 * The main.boundary.modelviewer package contains the ModelViewer class, which is responsible for
 * displaying single or lists of objects that implement the Displayable interface.
 */
package main.boundary.modelviewer;

import main.model.Displayable;
import main.model.camp.Camp;

import java.util.*;

/**
 * The ModelViewer class is responsible for displaying single or lists of
 * objects that implement the Displayable interface.
 */
public class ModelViewer {
    /**
     * Displays a single Displayable object.
     *
     * @param displayable The Displayable object to be displayed.
     */
    public static void displaySingleDisplayable(Displayable displayable) {
        System.out.println(displayable.getSplitter());
        System.out.print(displayable.getDisplayableString());
        System.out.println(displayable.getSplitter());
    }

    /**
     * Displays a list of Displayable objects.
     *
     * @param displayableList The list of Displayable objects to be displayed.
     */
    public static void displayListOfDisplayable(List<? extends Displayable> displayableList) {
        if (Objects.isNull(displayableList) || displayableList.isEmpty()) {
            System.out.println("Nothing found");
            return;
        }
        System.out.println(displayableList.get(0).getSplitter());
        for (Displayable displayable : displayableList) {
            System.out.print(displayable.getDisplayableString());
            System.out.println(displayable.getSplitter());
        }
    }

    /**
     * Displays a list of camps along with their types.
     *
     * @param campList A map containing Camp objects as keys and their corresponding types as values.
     */
    public static void displayListOfCampsWithType(Map<Camp, String> campList) {
        if (Objects.isNull(campList) || campList.isEmpty()) {
            System.out.println("Nothing found");
            return;
        }

        System.out.println("================================================================");

        for (Camp camp : campList.keySet()) {
            String type = campList.get(camp);
            System.out.print(camp.getDisplayableStringWithType(type));
            System.out.println(camp.getSplitter());
        }
    }
}
