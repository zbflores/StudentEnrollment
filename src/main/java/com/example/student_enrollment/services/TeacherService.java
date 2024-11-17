package com.example.student_enrollment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.repositories.TeacherRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    
    public Optional<Teacher> getTeacherById(Integer id) {
        return teacherRepository.findById(id);
    }
    
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    public Teacher updateTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    public void deleteTeacher(Integer id) {
        teacherRepository.deleteById(id);
    }
}