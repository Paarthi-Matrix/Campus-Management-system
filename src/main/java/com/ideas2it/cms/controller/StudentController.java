/*
 * StudentController
 *
 * Version 1.0.0
 *
 */

package com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ideas2it.cms.customexception.StudentDatabaseException;
import com.ideas2it.cms.customexception.HibernateDbConnectionException;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.Student;
import com.ideas2it.cms.service.StudentService;
import com.ideas2it.cms.util.DateUtil;

public class StudentController {

    private StudentService studentService = new StudentService();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Add a new student to the database.
     */
    public void addStudent() {
        String name;
        String dateOfBirth;
        String bloodGroup;
        int gradePreference;
        String gradeId;
        int specialclassAssigningCount = 2;
        String shirtSize;
        int shoeSize;
        int pantSize;

        System.out.print("Enter Student full name: ");
        name = scanner.nextLine();
        System.out.print("Enter Date of birth (dd/MM/yyyy): ");
        dateOfBirth = scanner.nextLine();
        if (!DateUtil.checkValidDate(dateOfBirth, "dd/MM/yyyy")) {
            while (true) {
                System.out.print("Invalid date..Kindly enter the valid date in format of (dd/MM/yyyy)");
                dateOfBirth = scanner.nextLine();
                if (DateUtil.checkValidDate(dateOfBirth, "dd/MM/yyyy")) {
                    break;
                }
            }
        }
        System.out.print("Enter the blood group: ");
        bloodGroup = scanner.nextLine();
        System.out.print("Enter the Grade: ");
        gradePreference = scanner.nextInt();
        Grade grade = null;
        try {
           grade = studentService.getPreferedGrade(gradePreference);
        } catch (StudentDatabaseException e) {
            System.out.println(e.getMessage());
        }
        if (grade == null) {
            System.out.println("No vacancy is available in any of the sections in grade " + gradePreference + " try another grade!");
            return;
        }
        gradeId = grade.getGradeId();
        Student student = studentService.addStudent(name, dateOfBirth, bloodGroup, gradeId, grade);
        System.out.println(name + " is added to the student database and assigned to " + student.getGrade().getStandard() +
                "th standard " + student.getGrade().getSection() + " section..!");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("The Special classes available are given below");
        int[] specialClassPreference = new int[specialclassAssigningCount];

        for (int i = 0; i < specialclassAssigningCount; i++) {
            int count = specialclassAssigningCount - i;
            System.out.println();
    	    System.out.println("===============================================");
    	    System.out.println("   Special Class Assignment");
    	    System.out.println("===============================================");
     	    System.out.println("Student can be assigned to " + count + " more special class" + (count > 1 ? "es" : "") + ".");
     	    System.out.println("Available options:");
            System.out.println("   1 - Music");
            System.out.println("   2 - Dance");
            System.out.println("   3 - Karate");
            System.out.println("   4 - Art & Painting");
            System.out.println("===============================================");
            System.out.print("Enter the student's preferred special class (1-4): ");
            specialClassPreference[i] = scanner.nextInt();
            scanner.nextLine();
            System.out.println("===============================================");
        }

        try {
            studentService.associateStudentToSpecialClass(student, specialClassPreference);
        } catch (StudentDatabaseException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("===============================================");
        System.out.println("Student successfully associated to special class!");
        System.out.println();
        System.out.println("===============================================");
        System.out.println("   Student Uniform Measurement Details");
        System.out.println("===============================================");
        System.out.print("Enter shirt size (XM, S, M, L, XL, XXL): ");
        shirtSize = scanner.nextLine();
        System.out.print("Enter pant size in centimeters: ");
        pantSize = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter shoe size (7 - 10): ");
        shoeSize = scanner.nextInt();
        System.out.println("===============================================");

        try {
            studentService.addUniformMeasurementToStudent(student, shirtSize, pantSize, shoeSize);
        } catch (StudentDatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a student from the database based on roll number.
     */
    public void deleteStudent() {
        System.out.print("Enter student roll number that to be deleted: ");
        String rollNumber = scanner.nextLine();
        Student student = null;
      
        try {
            student = studentService.getAndDeleteStudentByRollNumber(rollNumber);
        } catch (StudentDatabaseException e) {
            System.out.println(e.getMessage());
        }
        
        if (student == null) {
            System.out.println("No such student with roll number " + rollNumber + " is found in the database!");
        } else {
            System.out.println("Student " + student.getStudentName() + 
                               " with roll number " + rollNumber +
                               " is deleted successfully!");
        }
    }

    public void getStudentByGrade() {
        String requestedGrade = " ";
        String actionPreference;
        boolean validInput = false;

        System.out.println();
        System.out.println("===============================================");
        System.out.println("   Student Grade Inquiry");
        System.out.println("===============================================");
        System.out.print("Do you want to get the students by both grade and section? (Yes/Y or No/N): ");

        while (!validInput) {  /* Validate input for yes or no */
            actionPreference = scanner.nextLine().trim().toLowerCase();
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
                    System.out.println("Invalid input. Kindly enter Yes/Y or No/N.");
                    System.out.print("Do you want to get the students by both grade and section? (Yes/Y or No/N): ");
                    break;
             }
         }
         List<Student> students = new ArrayList<>();
         try {
             students = studentService.getStudentByGrade(requestedGrade);
         } catch (StudentDatabaseException ) {
             System.out.println(e.getMessage());
         }
         // Print the header for the student list
         System.out.println();
         System.out.println("===============================================");
         System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n", "Student ID", "Student Name", 
                                                                    "Roll Number", "Blood Group", 
                                                                    "Date of Birth", "Age");
         System.out.println("===============================================");
         for (Student student : students) {
             System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
             student.getStudentId(),
             student.getStudentName(),
             student.getRollNumber(),
             student.getBloodGroup(),
             student.getDateOfBirth(),
             DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "years"));
         }
         System.out.println("===============================================");
      }
 }
