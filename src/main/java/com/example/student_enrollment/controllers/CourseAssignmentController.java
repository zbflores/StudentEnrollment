package com.example.student_enrollment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.student_enrollment.models.CourseAssignment;
import com.example.student_enrollment.services.CourseAssignmentService;
import java.util.List;

@RestController
@RequestMapping("/api/course-assignments")
public class CourseAssignmentController {

    @Autowired
    private CourseAssignmentService courseAssignmentService;

    @GetMapping
    public List<CourseAssignment> getAllCourseAssignments() {
        return courseAssignmentService.getAllCourseAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseAssignment> getCourseAssignmentById(@PathVariable Integer id) {
        return courseAssignmentService.getCourseAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CourseAssignment createCourseAssignment(@RequestBody CourseAssignment assignment) {
        return courseAssignmentService.createCourseAssignment(assignment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseAssignment> updateCourseAssignment(@PathVariable Integer id, 
                                                                 @RequestBody CourseAssignment assignment) {
        return courseAssignmentService.getCourseAssignmentById(id)
                .map(existingAssignment -> {
                    // Copy new values to existing assignment
                    existingAssignment.setCourse(assignment.getCourse());
                    existingAssignment.setStartDate(assignment.getStartDate());
                    existingAssignment.setEndDate(assignment.getEndDate());
                    // Update using existing assignment
                    return ResponseEntity.ok(courseAssignmentService.updateCourseAssignment(existingAssignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseAssignment(@PathVariable Integer id) {
        return courseAssignmentService.getCourseAssignmentById(id)
                .map(assignment -> {
                    courseAssignmentService.deleteCourseAssignment(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}