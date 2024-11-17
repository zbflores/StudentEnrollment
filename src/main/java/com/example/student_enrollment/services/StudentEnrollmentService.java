package com.example.student_enrollment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.student_enrollment.models.StudentEnrollment;
import com.example.student_enrollment.repositories.StudentEnrollmentRepository;
import com.example.student_enrollment.repositories.StudentRepository;
import com.example.student_enrollment.repositories.CourseRepository;
import java.util.List;
import java.util.Optional;

@Service
public class StudentEnrollmentService {
    
    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<StudentEnrollment> getAllEnrollments() {
        return studentEnrollmentRepository.findAll();
    }
    
    public Optional<StudentEnrollment> getEnrollmentById(Integer id) {
        return studentEnrollmentRepository.findById(id);
    }
    
    public StudentEnrollment createEnrollment(StudentEnrollment enrollment) {
        // Verify student exists
        if (enrollment.getStudent() != null && enrollment.getStudent().getId() != null) {
            studentRepository.findById(enrollment.getStudent().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        }
        
        // Verify course exists
        if (enrollment.getCourse() != null && enrollment.getCourse().getId() != null) {
            courseRepository.findById(enrollment.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        }
        
        return studentEnrollmentRepository.save(enrollment);
    }
    
    public StudentEnrollment updateEnrollment(StudentEnrollment enrollment) {
        // Verify student exists
        if (enrollment.getStudent() != null && enrollment.getStudent().getId() != null) {
            studentRepository.findById(enrollment.getStudent().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        }
        
        // Verify course exists
        if (enrollment.getCourse() != null && enrollment.getCourse().getId() != null) {
            courseRepository.findById(enrollment.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        }
        
        return studentEnrollmentRepository.save(enrollment);
    }
    
    public void deleteEnrollment(Integer id) {
        studentEnrollmentRepository.deleteById(id);
    }
}