/*
 * MainController
 *
 * Version 1.0.0
 *
 */
 
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.Student;
import com.ideas2it.cms.controller.*;
import com.ideas2it.cms.helper.HibernateDbConnection;
import com.ideas2it.cms.service.StudentService;
import com.ideas2it.cms.service.GradeService;
import com.ideas2it.cms.util.DateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DashboardController {

    private static Scanner scanner = new Scanner(System.in);
    private StudentController studentController = new StudentController();
    private GradeController gradeController = new GradeController();

    public static void main(String[] args) {
        DashboardController dashboardController = new DashboardController();
        dashboardController.startApplication();
    }

    private void startApplication() {
        boolean loopCondition = true;

        while (loopCondition) {
            printMenu();

            try {
                int actionStatus = scanner.nextInt();
                scanner.nextLine(); 
                switch (actionStatus) {
                    case 1:
                        studentController.addStudent();
                        break;
                    case 2:
                        studentController.deleteStudent();
                        break;
                    case 3:
                        studentController.getStudentByGrade();
                        break;
                    case 4:
                        gradeController.getGradeInfo();
                        break;
                    case 5:
                        HibernateDbConnection.shutdown();
                        loopCondition = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // clear the invalid input
            }
        }
    }

    private void printMenu() {
        System.out.println("===================================================");
        System.out.println("|                                                 |");
        System.out.println("|              Campus Management System           |");
        System.out.println("|                                                 |");
        System.out.println("===================================================");
        System.out.println("|    Enter the action to perform:                 |");
        System.out.println("|    1 - Add a new student                        |");
        System.out.println("|    2 - Delete a student                         |");
        System.out.println("|    3 - Get students by grade                    |");
        System.out.println("|    4 - Get grade information                    |");
        System.out.println("|    5 - Exit Application                         |");
        System.out.println("===================================================");
        System.out.print("Select an option: ");
    }
}
