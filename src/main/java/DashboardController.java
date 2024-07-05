package com.ideas2it.cms;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.ideas2it.cms.controller.GradeController;
import com.ideas2it.cms.controller.StudentController;
import com.ideas2it.cms.helper.HibernateDbConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ideas2it.cms")
public class DashboardController implements CommandLineRunner {

    private static Scanner scanner = new Scanner(System.in);

    @Autowired
    private StudentController studentController;

    @Autowired
    private GradeController gradeController;

    public static void main(String[] args) {
        SpringApplication.run(DashboardController.class, args);
    }

    @Override
    public void run(String... args) {
        startApplication();
    }

    private void startApplication() {
        boolean loopCondition = true;

        while (loopCondition) {

            try {
                int actionStatus = scanner.nextInt();
                scanner.nextLine();
                switch (actionStatus) {
                    case 1:
                        //studentController.addStudent();
                        break;
                    case 2:
                        //studentController.deleteStudent();
                        break;
                    case 3:
                        //studentController.getAllStudents();
                    case 4:
                        //studentController.getStudentByGrade();
                        break;
                    case 5:
                        gradeController.getGradeInfo();
                        break;
                    case 6:
                        HibernateDbConnection.shutdown();
                        loopCondition = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // clear the invalid input
            }
        }
    }
}
