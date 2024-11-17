package com.example.student_enrollment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.services.TeacherService;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
        return teacherService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Integer id, 
                                               @RequestBody Teacher teacher) {
        return teacherService.getTeacherById(id)
                .map(existingTeacher -> {
                    // Copy new values to existing teacher
                    existingTeacher.setName(teacher.getName());
                    existingTeacher.setEmail(teacher.getEmail());
                    // Update using existing teacher
                    return ResponseEntity.ok(teacherService.updateTeacher(existingTeacher));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        return teacherService.getTeacherById(id)
                .map(teacher -> {
                    teacherService.deleteTeacher(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}