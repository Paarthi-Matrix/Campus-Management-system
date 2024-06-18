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

package src.com.ideas2it.cms.model;

public class UniformMeasurement {

    private Student student;
    private String shirtSize;
    private int pantSize;
    private int shoeSize;
    private String rollNumber;

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