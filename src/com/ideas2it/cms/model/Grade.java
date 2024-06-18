package src.com.ideas2it.cms.model;

import java.util.Set;

public class Grade {

    private String gradeId;
    private int vacancy;
    private int numberOfStudents;
    private String standard;
    private String section; 
    private Set<Student> students;

    public Grade(String gradeId, int vacancy, int numberOfStudents, String standard, String section) {
        this.gradeId = gradeId;
        this.vacancy = vacancy;
        this.numberOfStudents = numberOfStudents;
        this.standard = standard;
        this.section = section;
    }

    public Grade() {}

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getVacancy() {
        return vacancy;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
