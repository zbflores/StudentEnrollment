package com.example.student_enrollment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.services.CourseService;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Integer id, 
                                             @RequestBody Course course) {
        return courseService.getCourseById(id)
                .map(existingCourse -> {
                    // Copy new values to existing course
                    existingCourse.setName(course.getName());
                    existingCourse.setDescription(course.getDescription());
                    existingCourse.setCredits(course.getCredits());
                    existingCourse.setType(course.getType());
                    existingCourse.setStartDate(course.getStartDate());
                    existingCourse.setEndDate(course.getEndDate());
                    existingCourse.setTeacher(course.getTeacher());
                    // Update using existing course
                    return ResponseEntity.ok(courseService.updateCourse(existingCourse));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        return courseService.getCourseById(id)
                .map(course -> {
                    courseService.deleteCourse(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}