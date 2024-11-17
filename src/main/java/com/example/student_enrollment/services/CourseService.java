package com.example.student_enrollment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.repositories.CourseRepository;
import com.example.student_enrollment.repositories.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Integer id) {
        return courseRepository.findById(id);
    }
    
    public Course createCourse(Course course) {
        // Verify teacher exists
        if (course.getTeacher() != null && course.getTeacher().getId() != null) {
            teacherRepository.findById(course.getTeacher().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        }
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Course course) {
        // Verify teacher exists if teacher is being updated
        if (course.getTeacher() != null && course.getTeacher().getId() != null) {
            teacherRepository.findById(course.getTeacher().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        }
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}