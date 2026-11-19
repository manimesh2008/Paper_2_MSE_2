package org.example.controller;

import org.example.model.Student;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. Add a student (POST /api/students)
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            Student saved = studentService.addStudent(student);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Retrieve all students (GET /api/students)
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // 3. Search for a student by ID (GET /api/students/{id})
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with ID: " + id));
    }

    // Extra Search by Email (GET /api/students/search/email?email=...)
    @GetMapping("/search/email")
    public ResponseEntity<?> getStudentByEmail(@RequestParam String email) {
        return studentService.getStudentByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with email: " + email));
    }

    // Extra Search by Course (GET /api/students/search/course?course=...)
    @GetMapping("/search/course")
    public ResponseEntity<List<Student>> getStudentsByCourse(@RequestParam String course) {
        return ResponseEntity.ok(studentService.getStudentsByCourse(course));
    }

    // 4. Update student scores and skills (PUT /api/students/{id})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @RequestBody Student updatedData) {
        try {
            Student updated = studentService.updateStudent(id, updatedData);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. Delete a student record (DELETE /api/students/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully with ID: " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 6. Display the merit list (GET /api/students/merit-list)
    @GetMapping("/merit-list")
    public ResponseEntity<List<Student>> getMeritList() {
        return ResponseEntity.ok(studentService.getMeritList());
    }

    // Q2 (6): Students grouped by technical skills (GET /api/students/skills-group)
    @GetMapping("/skills-group")
    public ResponseEntity<?> getStudentsBySkills() {

        return ResponseEntity.ok(studentService.getStudentsGroupedBySkill());
    }

    // Q2 (7): Weak in Programming (<50) & Strong in Math (>=75) (GET /api/students/math-strong)
    @GetMapping("/math-strong")
    public ResponseEntity<List<Student>> getWeakProgStrongMath() {
        return ResponseEntity.ok(studentService.getWeakProgrammingStrongMathStudents());
    }
}
