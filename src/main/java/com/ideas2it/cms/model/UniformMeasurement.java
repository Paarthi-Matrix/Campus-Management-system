/**
 * 
 * <p>
 *
 * A UniformMeasurement object or an entity is defined as a collection of usefull information-
 * regarding uniform measurements of every students.
 * The measurement parameter contains `rollNumber`, `shirtSize`, `pantSize`, `shoeSize`.
 *
 * </p>
 *
 */

package com.ideas2it.cms.model;
import jakarta.persistence.*;

@Entity
@Table(name = "uniform_measurement_database")
public class UniformMeasurement {

    @Id
    @Column(name = "roll_number")
    private String rollNumber;

    @Column(name = "shirt_size", length = 4, nullable = false)
    private String shirtSize;

    @Column(name = "pant_size", nullable = false)
    private int pantSize;

    @Column(name = "shoe_size", nullable = false)
    private int shoeSize;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Constructors, getters, and setters
    public UniformMeasurement() {}

    public UniformMeasurement(String rollNumber, String shirtSize, int pantSize, int shoeSize) {
        this.rollNumber = rollNumber;
        this.shirtSize = shirtSize;
        this.pantSize = pantSize;
        this.shoeSize = shoeSize;
    }
    public String getRollNumber() {
       return this.rollNumber;
    }

    public void setRollNumber(String rollNumber) {
       this.rollNumber = rollNumber;
    }

    public String getShirtSize() {
        return this.shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public int getPantSize() {
       return this.pantSize;
    }

    public void setPantSize(int pantSize) {
       this.pantSize = pantSize;
    }
  
    public int getShoeSize() {
        return this.shoeSize;
    }

    public void setShoeSize(int shoeSize) {
        this.shoeSize = shoeSize;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}