package com.example.student_enrollment.controllers;

import com.example.student_enrollment.models.StudentEnrollment;
import com.example.student_enrollment.models.Student;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.services.StudentEnrollmentService;
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

@WebMvcTest(StudentEnrollmentController.class)
public class StudentEnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentEnrollmentService studentEnrollmentService;

    @Test
    public void createEnrollment_ShouldReturnCreatedEnrollment() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setName("Jane Smith");

        Course course = new Course();
        course.setId(1);
        course.setName("Introduction to Programming");

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(95.5f);

        when(studentEnrollmentService.createEnrollment(any(StudentEnrollment.class))).thenReturn(enrollment);

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"student\":{\"id\":1}," +
                        "\"course\":{\"id\":1}," +
                        "\"grade\":95.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student.id").value(1))
                .andExpect(jsonPath("$.course.id").value(1))
                .andExpect(jsonPath("$.grade").value(95.5));
    }

    @Test
    public void getEnrollment_ShouldReturnEnrollment() throws Exception {
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(1);

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(1);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(95.5f);

        when(studentEnrollmentService.getEnrollmentById(1)).thenReturn(Optional.of(enrollment));

        mockMvc.perform(get("/api/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.grade").value(95.5));
    }

    @Test
    public void getAllEnrollments_ShouldReturnList() throws Exception {
        StudentEnrollment enrollment1 = new StudentEnrollment();
        enrollment1.setId(1);
        enrollment1.setGrade(95.5f);

        StudentEnrollment enrollment2 = new StudentEnrollment();
        enrollment2.setId(2);
        enrollment2.setGrade(88.0f);

        when(studentEnrollmentService.getAllEnrollments()).thenReturn(Arrays.asList(enrollment1, enrollment2));

        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].grade").value(95.5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].grade").value(88.0));
    }

    @Test
    public void updateEnrollment_ShouldReturnUpdatedEnrollment() throws Exception {
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(1);

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(1);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(97.0f);

        when(studentEnrollmentService.getEnrollmentById(1)).thenReturn(Optional.of(enrollment));
        when(studentEnrollmentService.updateEnrollment(any(StudentEnrollment.class))).thenReturn(enrollment);

        mockMvc.perform(put("/api/enrollments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"student\":{\"id\":1}," +
                        "\"course\":{\"id\":1}," +
                        "\"grade\":97.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(97.0));
    }

    @Test
    public void deleteEnrollment_ShouldReturn200WhenSuccessful() throws Exception {
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(1);

        when(studentEnrollmentService.getEnrollmentById(1)).thenReturn(Optional.of(enrollment));

        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEnrollment_ShouldReturn404WhenNotFound() throws Exception {
        when(studentEnrollmentService.getEnrollmentById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enrollments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEnrollment_ShouldReturn404WhenNotFound() throws Exception {
        when(studentEnrollmentService.getEnrollmentById(999)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/enrollments/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"student\":{\"id\":1}," +
                        "\"course\":{\"id\":1}," +
                        "\"grade\":97.0}"))
                .andExpect(status().isNotFound());
    }
}