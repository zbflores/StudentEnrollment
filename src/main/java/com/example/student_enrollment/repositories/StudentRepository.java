package com.example.student_enrollment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_enrollment.models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}