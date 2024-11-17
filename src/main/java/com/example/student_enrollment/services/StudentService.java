package com.example.student_enrollment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.student_enrollment.models.Student;
import com.example.student_enrollment.repositories.StudentRepository;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }
    
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }
}