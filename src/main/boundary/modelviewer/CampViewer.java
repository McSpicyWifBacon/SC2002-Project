/**
 * The main.boundary.modelviewer package contains classes responsible for providing user interfaces
 * related to viewing details of camps.
 */
package main.boundary.modelviewer;

import main.controller.camp.CampManager;
import main.model.camp.Camp;
import main.model.user.Student;
import main.model.user.Staff;
import main.repository.camp.CampRepository;
import main.repository.user.StaffRepository;
import main.utils.exception.ModelNotFoundException;
import main.utils.exception.PageBackException;
import main.utils.iocontrol.IntGetter;
import main.utils.ui.BoundaryStrings;
import main.utils.ui.ChangePage;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

import java.util.Collections;
import java.util.Comparator;
/**
 * The CampViewer class displays details of camps and provides methods for viewing different lists of camps.
 */
public class CampViewer {

   /**
    * Displays the list of available camps.
    *
    * @throws PageBackException if the user wants to go back
    */
    public static void viewVisibleCampList() throws PageBackException {
        ChangePage.changePage();

        System.out.println("View Available Camp List");
        ModelViewer.displayListOfDisplayable(CampManager.viewAvailableCamps());

        System.out.println("Press Enter to go back.");
        new Scanner(System.in).nextLine();
        throw new PageBackException();
    }

    // FOR STUDENTS
    // /////////////////////////////////////////////////////////////////////////////////////////////
     /**
     * Displays the list of visible camps for a specific student.
     *
     * @param student the student to display the project details for
     * @throws PageBackException if the user wants to go back
     */
    public static void viewVisibleFacultyCampList(Student student) throws PageBackException {
        ChangePage.changePage();
        List<Camp> camps = CampManager.getCampsForStudent(student);

        // Sort camps alphabetically by default
        Comparator<Camp> campComparator = null;
        campComparator = Comparator.comparing(Camp::getCampName, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(camps, campComparator);

        System.out.println("List of Available Camps (Alphabetical Order):");
        ModelViewer.displayListOfDisplayable(camps);

        System.out.println("Choose an option:");
        System.out.println("\t1. Sort camps");
        System.out.println("\t2. Go Back");

        System.out.print("Enter your choice: ");
        int choice = IntGetter.readInt();

        switch (choice) {
            case 1:
                // Prompt the user to choose a sorting option
                sortCampsByOption(camps, student);
                break;
            case 2:
                throw new PageBackException();
            default:
                System.out.println("Invalid choice. Try again.");
                new Scanner(System.in).nextLine();
                viewVisibleFacultyCampList(student);
        }
    }

    /**
     * Sorts the list of camps based on the user's choice and displays the sorted list.
     *
     * @param camps   the list of camps to be sorted
     * @param student the student for whom the camps are being sorted
     * @throws PageBackException if the user wants to go back
     */
    private static void sortCampsByOption(List<Camp> camps, Student student) throws PageBackException {
        ChangePage.changePage();
        System.out.println("Sort camps by:");
        System.out.println("\t1. Camp ID");
        System.out.println("\t2. Camp Name");
        System.out.println("\t3. Camp Date");
        System.out.println("\t4. Camp Closing Date");
        System.out.println("\t5. Camp Location");
        System.out.println("\t6. Go Back");

        System.out.print("Enter your choice: ");
        int sortChoice = IntGetter.readInt();

        if (sortChoice == 6) {
            viewVisibleFacultyCampList(student);
        }

        String sortTitle = "List of Available Camps";

        Comparator<Camp> campComparator = null;

        switch (sortChoice) {
            case 1:
                sortTitle = "List of Available Camps (ID order):";
                campComparator = Comparator.comparingInt(camp -> {
                    try {
                        return Integer.parseInt(camp.getID().replaceAll("\\D", ""));
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE;
                    }
                });
                break;
            case 2:
                sortTitle = "List of Available Camps (Alphabetical order):";
                campComparator = Comparator.comparing(Camp::getCampName, String.CASE_INSENSITIVE_ORDER);
                break;
            case 3:
                sortTitle = "List of Available Camps (Date order):";
                campComparator = (camp1, camp2) -> {
                    String date1 = camp1.getDates().substring(0, 8);
                    String date2 = camp2.getDates().substring(0, 8);
                    return date1.compareTo(date2);
                };

                break;
            case 4:
                sortTitle = "List of Available Camps (Closing Date order):";
                campComparator = Comparator.comparing(Camp::getRegistrationClosingDate);
                break;
            case 5:
                sortTitle = "List of Available Camps (Location order):";
                campComparator = Comparator.comparing(Camp::getLocation);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                new Scanner(System.in).nextLine();
                viewVisibleFacultyCampList(student);
        }

        Collections.sort(camps, campComparator);

        ChangePage.changePage();
        System.out.println(sortTitle);
        ModelViewer.displayListOfDisplayable(camps);

        System.out.println("Press Enter to go back.");
        new Scanner(System.in).nextLine();
        throw new PageBackException();
    }

    public static void viewCamp(Camp camp) throws PageBackException {
        ChangePage.changePage();
        System.out.println("View Camp");
        ModelViewer.displaySingleDisplayable(camp);
        System.out.println("Press Enter to go back.");
        new Scanner(System.in).nextLine();
        throw new PageBackException();
    }

    /**
     * Displays the list of camps for a specific staff member.
     *
     * @param staff the staff member for whom the camps are displayed
     * @throws PageBackException if the user wants to go back
     */
    public static void viewStaffCamps(Staff staff) throws PageBackException {
        List<Camp> camps = CampRepository.getInstance()
                .findByRules(camp -> Objects.equals(camp.getStaffID(), staff.getID()));
        System.out.println("Here are all your camps:");
        ModelViewer.displayListOfDisplayable(camps);
    }

    /**
     * Displays the list of camps registered by a specific student.
     *
     * @param student the student for whom the registered camps are displayed
     * @throws PageBackException if the user wants to go back
     */
    public static void viewStudentCamps(Student student) throws PageBackException {
        ChangePage.changePage();
        System.out.println("View Registered Camps");
        Map<Camp, String> camps = CampManager.getStudentcamps(student);
        if (camps == null) {
            System.out.println("Student has not registered into any camps yet.");
        } else {
            ModelViewer.displayListOfCampsWithType(camps);
        }
        System.out.println("Press Enter to go back.");
        new Scanner(System.in).nextLine();
        throw new PageBackException();
    }

    /**
     * Selects the type of camps to view for a specific staff member and displays the selected list.
     *
     * @param staff the staff member for whom the camps are displayed
     * @throws PageBackException if the user wants to go back
     */
    public static void selectCampTypeAndDisplay(Staff staff) throws PageBackException {
        ChangePage.changePage();

        System.out.println("Select the type of camps to view:");
        System.out.println("\t1. View All Camps");
        System.out.println("\t2. View Visible Camps");
        System.out.println("\t3. View Invisible Camps");
        System.out.println("\t4. Go Back");

        System.out.print("Enter your choice: ");
        int typeChoice = IntGetter.readInt();

        switch (typeChoice) {
            case 1:
                viewAllCamps();
                break;
            case 2:
                viewCampList(staff, CampManager.getAllVisibleCamps(), true);
                break;
            case 3:
                viewCampList(staff, CampManager.getAllInvisibleCamps(), false);
                break;
            case 4:
                throw new PageBackException();
            default:
                System.out.println("Invalid choice. Try again.");
                new Scanner(System.in).nextLine();
                selectCampTypeAndDisplay(staff);
        }
    }

    /**
     * Displays a list of camps based on the specified criteria for a specific staff member.
     *
     * @param staff    the staff member for whom the camps are displayed
     * @param camps    the list of camps to be displayed
     * @param isVisible indicates whether the camps are visible or invisible
     * @throws PageBackException if the user wants to go back
     */
    public static void viewCampList(Staff staff, List<Camp> camps, boolean isVisible) throws PageBackException {
        ChangePage.changePage();

        Comparator<Camp> campComparator = Comparator.comparing(Camp::getCampName, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(camps, campComparator);

        System.out.println("List of Available Camps (Alphabetical Order):");
        ModelViewer.displayListOfDisplayable(camps);

        System.out.println("Choose an option:");
        System.out.println("\t1. Sort camps");
        System.out.println("\t2. Go Back");

        System.out.print("Enter your choice: ");
        int choice = IntGetter.readInt();

        switch (choice) {
            case 1:
                sortCampsByOption(camps, staff, isVisible);
                break;
            case 2:
                throw new PageBackException();
            default:
                System.out.println("Invalid choice. Try again.");
                new Scanner(System.in).nextLine();
                throw new PageBackException();
        }
    }

    /**
     * Sorts the list of camps based on the user's choice and displays the sorted list for a specific staff member.
     *
     * @param camps    the list of camps to be sorted
     * @param staff    the staff member for whom the camps are being sorted
     * @param isVisible indicates whether the camps are visible or invisible
     * @throws PageBackException if the user wants to go back
     */
    private static void sortCampsByOption(List<Camp> camps, Staff staff, boolean isVisible) throws PageBackException {
        ChangePage.changePage();
        System.out.println("Sort camps by:");
        System.out.println("\t1. Camp ID");
        System.out.println("\t2. Camp Name");
        System.out.println("\t3. Camp Date");
        System.out.println("\t4. Camp Closing Date");
        System.out.println("\t5. Camp Location");
        System.out.println("\t6. Go Back");

        System.out.print("Enter your choice: ");
        int sortChoice = IntGetter.readInt();

        if (sortChoice == 6) {
            viewCampList(staff, camps, isVisible);
        }

        String sortTitle = "List of Available Camps";

        Comparator<Camp> campComparator = null;

        switch (sortChoice) {
            case 1:
                sortTitle = "List of Available Camps (ID order):";
                campComparator = Comparator.comparingInt(camp -> {
                    try {
                        return Integer.parseInt(camp.getID().replaceAll("\\D", ""));
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE;
                    }
                });
                break;
            case 2:
                sortTitle = "List of Available Camps (Alphabetical order):";
                campComparator = Comparator.comparing(Camp::getCampName, String.CASE_INSENSITIVE_ORDER);
                break;
            case 3:
                sortTitle = "List of Available Camps (Date order):";
                campComparator = Comparator.comparing(camp -> camp.getDates().substring(0, 8));
                break;
            case 4:
                sortTitle = "List of Available Camps (Closing Date order):";
                campComparator = Comparator.comparing(Camp::getRegistrationClosingDate);
                break;
            case 5:
                sortTitle = "List of Available Camps (Location order):";
                campComparator = Comparator.comparing(Camp::getLocation);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                new Scanner(System.in).nextLine();
                viewCampList(staff, camps, isVisible);
        }

        Collections.sort(camps, campComparator);

        ChangePage.changePage();
        System.out.println(sortTitle);
        ModelViewer.displayListOfDisplayable(camps);

        System.out.println("Press Enter to go back.");
        new Scanner(System.in).nextLine();
        throw new PageBackException();
    }

    /**
     * Displays a list of all camps.
     *
     * @throws PageBackException if the user wants to go back
     */
    public static void viewAllCamps() throws PageBackException {
        viewCampList(null, CampManager.viewAllcamp(), false);
    }

    /**
     * Displays a list of visible camps for a specific staff member.
     *
     * @param staff the staff member for whom the camps are displayed
     * @throws PageBackException if the user wants to go back
     */
    public static void viewVisibleCampList(Staff staff) throws PageBackException {
        viewCampList(staff, CampManager.getAllVisibleCamps(), true);
    }
    
    /**
     * Displays a list of invisible camps for a specific staff member.
     *
     * @param staff the staff member for whom the camps are displayed
     * @throws PageBackException if the user wants to go back
     */
    public static void viewInvisibleCampList(Staff staff) throws PageBackException {
        viewCampList(staff, CampManager.getAllInvisibleCamps(), false);
    }

}
