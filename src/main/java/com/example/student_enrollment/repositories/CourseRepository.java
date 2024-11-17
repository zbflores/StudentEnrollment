package com.example.student_enrollment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_enrollment.models.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}