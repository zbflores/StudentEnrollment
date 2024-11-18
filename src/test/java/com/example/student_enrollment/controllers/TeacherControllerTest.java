package com.example.student_enrollment.controllers;

import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Test
    public void createTeacher_ShouldReturnCreatedTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("John Doe");
        teacher.setEmail("john@example.com");

        when(teacherService.createTeacher(any(Teacher.class))).thenReturn(teacher);

        mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void getTeacher_ShouldReturnTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("John Doe");
        teacher.setEmail("john@example.com");

        when(teacherService.getTeacherById(1)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void getAllTeachers_ShouldReturnList() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setName("John Doe");
        Teacher teacher2 = new Teacher();
        teacher2.setName("Jane Doe");

        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(teacher1, teacher2));

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }
}