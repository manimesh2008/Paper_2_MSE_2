package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Q2 (8) & Q3 (1): Add Student with Duplicate ID and Score Validation (0 to 100)
    public Student addStudent(Student student) {
        if (student.getStudentId() == null || studentRepository.existsById(student.getStudentId())) {
            throw new IllegalArgumentException("Student ID is missing or already exists (Duplicate ID not allowed).");
        }
        validateScores(student.getProgrammingScore(), student.getMathematicsScore());
        return studentRepository.save(student);
    }

    private void validateScores(double progScore, double mathScore) {
        if (progScore < 0 || progScore > 100 || mathScore < 0 || mathScore > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100.");
        }
    }

    // Q3 (2): Retrieve All Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Q2 (4) & Q3 (3): Search by ID, Email, or Course
    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public List<Student> getStudentsByCourse(String courseName) {
        return studentRepository.findByCourseNameIgnoreCase(courseName);
    }

    // Q3 (4): Update Student Scores and Skills
    public Student updateStudent(Integer id, Student updatedData) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + id));

        validateScores(updatedData.getProgrammingScore(), updatedData.getMathematicsScore());

        existing.setProgrammingScore(updatedData.getProgrammingScore());
        existing.setMathematicsScore(updatedData.getMathematicsScore());
        if (updatedData.getTechnicalSkills() != null) {
            existing.setTechnicalSkills(updatedData.getTechnicalSkills());
        }
        return studentRepository.save(existing);
    }

    // Q3 (5): Delete Student Record
    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new NoSuchElementException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Q2 (5) & Q3 (6): Merit List sorted by Average Score (Descending) using List & Comparator
    public List<Student> getMeritList() {
        List<Student> allStudents = new ArrayList<>(studentRepository.findAll());
        allStudents.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return allStudents;
    }

    // Q2 (6): Group Students with Same Technical Skills using HashMap<String, List<String>>
    public Map<String, List<String>> getStudentsGroupedBySkill() {
        List<Student> allStudents = studentRepository.findAll();
        Map<String, List<String>> skillMap = new HashMap<>();

        for (Student s : allStudents) {
            for (String skill : s.getTechnicalSkills()) {
                String normalizedSkill = skill.trim().toUpperCase();
                skillMap.computeIfAbsent(normalizedSkill, k -> new ArrayList<>())
                        .add(s.getStudentName() + " (ID: " + s.getStudentId() + ")");
            }
        }
        return skillMap;
    }

    // Q2 (7): Students needing improvement in Programming (< 50) but Mathematics >= 75
    public List<Student> getWeakProgrammingStrongMathStudents() {
        List<Student> allStudents = studentRepository.findAll();
        List<Student> result = new ArrayList<>();

        for (Student s : allStudents) {
            if (s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75) {
                result.add(s);
            }
        }
        return result;
    }
}
