/*
 * StudentController
 *
 * Version 1.0.0
 *
 */

package com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ideas2it.cms.customexception.StudentDatabaseException;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.Student;
import com.ideas2it.cms.service.StudentService;
import com.ideas2it.cms.util.BloodgroupUtil;
import com.ideas2it.cms.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private StudentService studentService = new StudentService();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Add a new student to the database.
     */
    public void addStudent() {
        logger.info("The application entered the insertion phase");
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
                logger.warn("User entered invalid date {}", dateOfBirth);
                System.out.println("Kindly enter the valid date in format of (dd/MM/yyyy)");
                dateOfBirth = scanner.nextLine();
                if (DateUtil.checkValidDate(dateOfBirth, "dd/MM/yyyy")) {
                    break;
                }
            }
        }
        System.out.print("Enter the blood group: ");
        bloodGroup = BloodgroupUtil.validateBloodGroup(scanner.nextLine());
        if (null == bloodGroup) {
            while (true) {
                System.out.println("Enter a valid blood group");
                System.out.println("The valid blood group are given below");
                System.out.println("A+VE");
                System.out.println("A-VE");
                System.out.println("B+VE");
                System.out.println("B-VE");
                System.out.println("AB+VE");
                System.out.println("AB-VE");
                System.out.println("O+VE");
                System.out.print("O-VE");

                bloodGroup = BloodgroupUtil.validateBloodGroup(scanner.nextLine());
                if (null != bloodGroup) {
                    break;
                }
            }
        }
        System.out.print("Enter the Grade: ");
        gradePreference = scanner.nextInt();
        Grade grade = null;
        try {
            logger.info("Adding student {}", name);
            grade = studentService.getPreferedGrade(gradePreference);
        } catch (StudentDatabaseException e) {
            logger.error(e.getMessage());
        }
        if (grade == null) {
            logger.warn("No vacancy is available for the prefered grade {}", gradePreference);
        }
        gradeId = grade.getGradeId();
        Student student = studentService.addStudent(name, dateOfBirth, bloodGroup, gradeId, grade);
        logger.info(name + " is added to the student database and assigned to " +
                student.getGrade().getStandard() + "th standard " + student.getGrade().getSection() + " section..!");
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
            logger.info("Asoociaitng student {} to the prefered special classes", name);
            studentService.associateStudentToSpecialClass(student, specialClassPreference);
        } catch (StudentDatabaseException e) {
            logger.error(e.getMessage());
        }
        System.out.println();
        System.out.println("===============================================");
        logger.info("Student {} successfully associated to special class!", student.getStudentName());
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
            logger.error(e.getMessage());
        }
        logger.info("Student {} uniform details entered successfully!", student.getStudentName());
    }

    /**
     * Delete a student from the database based on roll number.
     */
    public void deleteStudent() {
        logger.info("The application entered the deleting the record phase");
        System.out.print("Enter student roll number that to be deleted: ");
        String rollNumber = scanner.nextLine();
        Student student = null;
      
        try {
            student = studentService.getAndDeleteStudentByRollNumber(rollNumber);
        } catch (StudentDatabaseException e) {
            logger.error(e.getMessage());
        }
        
        if (student == null) {
            logger.warn("No such student with roll number {} is found in the database!", rollNumber);
        } else {
            logger.info("Student " + student.getStudentName() +
                               " with roll number " + rollNumber +
                               " is deleted successfully!");
        }
    }

    public void getStudentByGrade() {
        logger.info("The application entered the process of fetching of getting the grade by student.");
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
                    logger.warn("Invalid input. Kindly enter Yes/Y or No/N.");
                    System.out.print("Do you want to get the students by both grade and section? (Yes/Y or No/N): ");
                    break;
             }
         }
         List<Student> students = new ArrayList<>();
         try {
             students = studentService.getStudentByGrade(requestedGrade);
         } catch (StudentDatabaseException e ) {
             logger.error(e.getMessage());
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
