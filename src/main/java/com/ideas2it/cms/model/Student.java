/**
 * 
 * <p>
 * A student object or an entity is defined as a collection of usefull information of individual students.
 * The usefull information contains `studentName`, `rollNumber`, `gradeID`, `grade`, `section`, `bloodGroup`, `dateOfBirth` and `age`.
 * </p>
 *
 */
package com.ideas2it.cms.model;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "student_database")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int studentId;

    @Column(name = "name", length = 30, nullable = false)
    private String studentName;

    @Column(name = "roll_number", length = 6, nullable = false)
    private String rollNumber;

    @Column(name = "blood_group", length = 5, nullable = false)
    private String bloodGroup;

    @Column(name = "date_of_birth", length = 10, nullable = false)
    private String dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_specialclass",
            joinColumns = @JoinColumn(name = "student_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "specialclass_id", nullable = false)
    )
    private Set<SpecialClass> specialClass;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
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
