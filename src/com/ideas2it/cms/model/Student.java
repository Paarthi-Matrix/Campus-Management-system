
/**
 * 
 * <p>
 *
 * A student object or an entity is defined as a collection of usefull information of individual students.
 * The usefull information contains `studentName`, `rollNumber`, `gradeID`, `grade`, `section`, `bloodGroup`, `dateOfBirth` and `age`.
 *
 * </p>
 *
 */

package src.com.ideas2it.cms.model;


import java.util.Set;

public class Student {

    private int studentId;
    private String studentName;
    private String rollNumber;
    private String bloodGroup;
    private String dateOfBirth;
    private Grade grade;
    private Set<SpecialClass> specialClass;
    private UniformMeasurement uniformMeasurement;

    public Student(String studentName, String rollNumber, String bloodGroup, String dateOfBirth) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
    }

    public Student() {}

    public UniformMeasurement getUniformMeasurement() {
        return this.uniformMeasurement;
    }

    public void setUniformMeasurement(UniformMeasurement uniformMeasurement) {
        this.uniformMeasurement = uniformMeasurement;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Set<SpecialClass> getSpecialClass() {
        return this.specialClass;
    }

    public void setSpecialClass(Set<SpecialClass> specialClass) {
        this.specialClass = specialClass;
    }
}
