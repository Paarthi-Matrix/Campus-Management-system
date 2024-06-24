/**
 * 
 * <p>
 * A Grade object or an entity is defined as a collection of usefull information of special classes.
 * The usefull information contains `specialClassID`, `className`, `vaccancy`, `numberOfStudents` and `handlingStaff`.
 * </p>
 *
 */
package com.ideas2it.cms.model;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "specialclass_database")
public class SpecialClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialclass_id")
    private int specialClassId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "vacancy")
    private int vacancy;

    @Column(name = "number_of_students")
    private int numberOfStudents;

    @Column(name = "handling_staff")
    private String handlingStaff;

    @ManyToMany(mappedBy = "specialClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Student> students;

    // Constructors, getters, and setters
    public SpecialClass() {}

    public SpecialClass(int specialClassId, String className, int vacancy, int numberOfStudents, String handlingStaff) {
        this.specialClassId = specialClassId;
        this.className = className;
        this.vacancy = vacancy;
        this.numberOfStudents = numberOfStudents;
        this.handlingStaff = handlingStaff;
    }
    public int getSpecialClassId() {
        return this.specialClassId;
    }

    public void setSpecialClassId(int specialClassId) {
        this.specialClassId = specialClassId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getVacancy() {
        return this.vacancy;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }

    public int getNumberOfStudents() {
        return this.numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String  getHandlingStaff() {
        return this.handlingStaff;
    }

    public void setHandlingStaff(String handlingStaff) {
        this.handlingStaff = handlingStaff;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}