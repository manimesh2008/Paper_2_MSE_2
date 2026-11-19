package org.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private Integer studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(unique = true, nullable = false)
    private String email;

    private String courseName;
    private double programmingScore;
    private double mathematicsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> technicalSkills = new ArrayList<>();

    // Default Constructor
    public Student() {}

    // Parameterized Constructor
    public Student(Integer studentId, String studentName, String email, String courseName,
                   double programmingScore, double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills != null ? technicalSkills : new ArrayList<>();
    }

    // Q2 (1): Calculate Average Score
    public double getAverageScore() {
        return Math.round(((programmingScore + mathematicsScore) / 2.0) * 100.0) / 100.0;
    }

    // Q2 (2): Assign Performance Category
    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85) {
            return "Excellent";
        } else if (avg >= 70) {
            return "Good";
        } else if (avg >= 50) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }

    // Q2 (3): Identify Stronger Subject
    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore) {
            return "Programming";
        } else if (mathematicsScore > programmingScore) {
            return "Mathematics";
        } else {
            return "Equal in Both";
        }
    }

    // Getters and Setters
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double programmingScore) { this.programmingScore = programmingScore; }

    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double mathematicsScore) { this.mathematicsScore = mathematicsScore; }

    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", averageScore=" + getAverageScore() +
                ", category='" + getPerformanceCategory() + '\'' +
                '}';
    }
}
