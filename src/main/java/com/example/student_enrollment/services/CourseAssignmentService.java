package com.example.student_enrollment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.student_enrollment.models.CourseAssignment;
import com.example.student_enrollment.repositories.CourseAssignmentRepository;
import com.example.student_enrollment.repositories.CourseRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CourseAssignmentService {
    
    @Autowired
    private CourseAssignmentRepository courseAssignmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<CourseAssignment> getAllCourseAssignments() {
        return courseAssignmentRepository.findAll();
    }
    
    public Optional<CourseAssignment> getCourseAssignmentById(Integer id) {
        return courseAssignmentRepository.findById(id);
    }
    
    public CourseAssignment createCourseAssignment(CourseAssignment assignment) {
        // Verify course exists
        if (assignment.getCourse() != null && assignment.getCourse().getId() != null) {
            courseRepository.findById(assignment.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        }
        return courseAssignmentRepository.save(assignment);
    }
    
    public CourseAssignment updateCourseAssignment(CourseAssignment assignment) {
        // Verify course exists if course is being updated
        if (assignment.getCourse() != null && assignment.getCourse().getId() != null) {
            courseRepository.findById(assignment.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        }
        return courseAssignmentRepository.save(assignment);
    }
    
    public void deleteCourseAssignment(Integer id) {
        courseAssignmentRepository.deleteById(id);
    }
}