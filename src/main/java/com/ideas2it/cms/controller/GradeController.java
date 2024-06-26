package com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.ideas2it.cms.customexception.GradeDatabaseException;
import com.ideas2it.cms.customexception.HibernateDbConnectionException;
import com.ideas2it.cms.helper.HibernateDbConnection;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.service.GradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradeController {

    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);
    private GradeService gradeService = new GradeService();
    private Scanner scanner = new Scanner(System.in);

    /**
     * <p>
     * Responsible for Grade Inquiry. 
     * If you want to get the grade based on GRADE ONLY, give No as input.
     * If you want to get the grade baed on BOTH GRADE AND SECTION, give Yes as input.
     * </p>
     */ 
    public void getGradeInfo() {
       String requestedGrade = "";
       boolean validInput = false;
       List<Grade> grades = new ArrayList<>();;
       System.out.println();
       System.out.println("===============================================");
       System.out.println("               Grade Inquiry");
       System.out.println("===============================================");
       System.out.print("Do you want to get the grade details by section? (Yes/Y or No/N): ");
  
       while (!validInput) {  /* Validate input for yes or no */
           String actionPreference = scanner.nextLine().trim().toLowerCase();
           switch (actionPreference) {
               case "y":
               case "yes":
                   System.out.print("Enter the standard and section without spaces: ");
                   requestedGrade = scanner.nextLine().trim();
                   validInput = true;
                   break;
               case "n":
               case "no":
                   System.out.print("Enter the standard: ");
                   requestedGrade = scanner.nextLine().trim();
                   validInput = true;
                   break;
               default:
                   logger.warn("Invalid input given by the user {}", actionPreference);
                   System.out.println("Kindly enter Yes/Y or No/N.");
                   System.out.print("Do you want to get the students by both grade and section? (Yes/Y or No/N): ");
                   break;
            }
        }

       try {
           logger.debug("Fetching the grade according to the prefered grade {}...", requestedGrade);
           grades = gradeService.getGradeInfo(requestedGrade);
           logger.debug("Fetching of grade {} is successfully done!...", requestedGrade);
       } catch (GradeDatabaseException | HibernateDbConnectionException e) {
            logger.error(e.getMessage());
       }

       // Print the header for the student list
        System.out.println();
        System.out.println("===============================================");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", "Grade ID", "Vacancy", "Number of Students", "Standard", 
                                                                   "Section");
        System.out.println("===============================================");
        for (Grade grade : grades) {
            System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
            grade.getGradeId(),
            grade.getVacancy(),
            grade.getNumberOfStudents(),
            grade.getStandard(),
            grade.getSection());
        }
        System.out.println("===============================================");
     }          
}