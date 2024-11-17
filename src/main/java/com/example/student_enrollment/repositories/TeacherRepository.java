package com.example.student_enrollment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_enrollment.models.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}