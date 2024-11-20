package com.example.student_enrollment.controllers;

import com.example.student_enrollment.models.Student;
import com.example.student_enrollment.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void createStudent_ShouldReturnCreatedStudent() throws Exception {
        Student student = new Student();
        student.setName("Jane Smith");
        student.setEmail("jane@example.com");
        student.setBirthDate(LocalDate.of(2000, 1, 15));
        student.setGradeLevel("FRESHMAN");

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jane Smith\",\"email\":\"jane@example.com\",\"birthDate\":\"2000-01-15\",\"gradeLevel\":\"FRESHMAN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.gradeLevel").value("FRESHMAN"));
    }

    @Test
    public void getStudent_ShouldReturnStudent() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setName("Jane Smith");
        student.setEmail("jane@example.com");
        student.setBirthDate(LocalDate.of(2000, 1, 15));
        student.setGradeLevel("FRESHMAN");

        when(studentService.getStudentById(1)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void getAllStudents_ShouldReturnList() throws Exception {
        Student student1 = new Student();
        student1.setName("Jane Smith");
        student1.setGradeLevel("FRESHMAN");
        
        Student student2 = new Student();
        student2.setName("John Doe");
        student2.setGradeLevel("SOPHOMORE");

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].name").value("John Doe"));
    }

    @Test
    public void deleteStudent_ShouldReturn200WhenSuccessful() throws Exception {
        Student student = new Student();
        student.setId(1);
        
        when(studentService.getStudentById(1)).thenReturn(Optional.of(student));

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudent_ShouldReturn404WhenNotFound() throws Exception {
        when(studentService.getStudentById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound());
    }
}