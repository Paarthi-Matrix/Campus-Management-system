package src.com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.com.ideas2it.cms.customexception.GradeDatabaseException;
import src.com.ideas2it.cms.customexception.HibernateDbConnectionException;
import src.com.ideas2it.cms.model.Grade;
import src.com.ideas2it.cms.service.GradeService;


public class GradeController {

    private GradeService gradeService = new GradeService();
    private Scanner scanner = new Scanner(System.in);

    public void getGradeInfo() {

       String gradeId;
       String requestedGrade = "";
       boolean validInput = false;
       List<Grade> grades = new ArrayList<>();;
       System.out.println();
       System.out.println("===============================================");
       System.out.println("   Grade Inquiry");
       System.out.println("===============================================");
       System.out.print("Do you want to get the grade details by section? (Yes/Y or No/N): ");

       
       while (!validInput) {          /* Validate input for yes or no */

           String actionPreference = scanner.nextLine().trim().toLowerCase();
           switch (actionPreference) {
               case "Y":
               case "Yes":
               case "y":
               case "yes":
                   System.out.print("Enter the standard and section without spaces: ");
                   requestedGrade = scanner.nextLine().trim();
                   validInput = true;
                   break;
               case "N":
               case "No":
               case "n":
               case "no":
                   System.out.print("Enter the standard: ");
                   requestedGrade = scanner.nextLine().trim();
                   validInput = true;
                   break;
               default:
                   System.out.println("Invalid input. Kindly enter Yes/Y or No/N.");
                   System.out.print("Do you want to get the students by both grade and section? (Yes/Y or No/N): ");
                   break;
            }
        }

        try {
            grades = gradeService.getGradeInfo(requestedGrade);
        } catch (GradeDatabaseException | HibernateDbConnectionException e) {
            System.out.println(e.getMessage());
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

