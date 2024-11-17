package com.example.student_enrollment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.student_enrollment.models.StudentEnrollment;
import com.example.student_enrollment.services.StudentEnrollmentService;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class StudentEnrollmentController {

    @Autowired
    private StudentEnrollmentService studentEnrollmentService;

    @GetMapping
    public List<StudentEnrollment> getAllEnrollments() {
        return studentEnrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollment> getEnrollmentById(@PathVariable Integer id) {
        return studentEnrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StudentEnrollment createEnrollment(@RequestBody StudentEnrollment enrollment) {
        return studentEnrollmentService.createEnrollment(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollment> updateEnrollment(@PathVariable Integer id, 
                                                            @RequestBody StudentEnrollment enrollment) {
        return studentEnrollmentService.getEnrollmentById(id)
                .map(existingEnrollment -> {
                    // Copy new values to existing enrollment
                    existingEnrollment.setStudent(enrollment.getStudent());
                    existingEnrollment.setCourse(enrollment.getCourse());
                    existingEnrollment.setGrade(enrollment.getGrade());
                    // Update using existing enrollment
                    return ResponseEntity.ok(studentEnrollmentService.updateEnrollment(existingEnrollment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Integer id) {
        return studentEnrollmentService.getEnrollmentById(id)
                .map(enrollment -> {
                    studentEnrollmentService.deleteEnrollment(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}