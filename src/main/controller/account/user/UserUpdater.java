package main.controller.account.user;

import main.model.user.Student;
import main.model.user.Staff;
import main.model.user.User;
import main.repository.user.StudentRepository;
import main.repository.user.StaffRepository;
import main.utils.exception.ModelNotFoundException;

/**
 * The UserUpdater class offers a utility for updating users in the database.
 */
public class UserUpdater {
    /**
     * Updates the specific user in the database.
     *
     * @param student the user to be updated
     * @throws ModelNotFoundException if the user is not found in the database
     */
    private static void updateStudent(Student student) throws ModelNotFoundException {
        StudentRepository.getInstance().update(student);
    }

    /**
     * Updates the specified staff in the database.
     *
     * @param staff the staff to be updated
     * @throws ModelNotFoundException if the staff is not found in the database
     */
    private static void updateStaff(Staff staff) throws ModelNotFoundException {
        StaffRepository.getInstance().update(staff);
    }

    /**
     * Updates the specified user in the database.
     *
     * @param user the user to be updated
     * @throws ModelNotFoundException if the user is not found in the database
     */
    public static void updateUser(User user) throws ModelNotFoundException {
        if (user instanceof Student student) {
            updateStudent(student);
        } else if (user instanceof Staff staff) {
            updateStaff(staff);
        }
    }
}
