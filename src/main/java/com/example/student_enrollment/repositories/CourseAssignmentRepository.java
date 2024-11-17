package com.example.student_enrollment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_enrollment.models.CourseAssignment;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Integer> {
}