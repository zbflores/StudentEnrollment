package com.example.student_enrollment.controllers;

import com.example.student_enrollment.models.CourseAssignment;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.services.CourseAssignmentService;
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

@WebMvcTest(CourseAssignmentController.class)
public class CourseAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseAssignmentService courseAssignmentService;

    @Test
    public void createAssignment_ShouldReturnCreatedAssignment() throws Exception {
        Course course = new Course();
        course.setId(1);
        course.setName("Java Programming");

        CourseAssignment assignment = new CourseAssignment();
        assignment.setCourse(course);
        assignment.setStartDate(LocalDate.of(2024, 1, 15));
        assignment.setEndDate(LocalDate.of(2024, 2, 15));

        when(courseAssignmentService.createCourseAssignment(any(CourseAssignment.class))).thenReturn(assignment);

        mockMvc.perform(post("/api/course-assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"course\":{\"id\":1}," +
                        "\"startDate\":\"2024-01-15\"," +
                        "\"endDate\":\"2024-02-15\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.id").value(1))
                .andExpect(jsonPath("$.startDate").value("2024-01-15"))
                .andExpect(jsonPath("$.endDate").value("2024-02-15"));
    }

    @Test
    public void getAssignment_ShouldReturnAssignment() throws Exception {
        Course course = new Course();
        course.setId(1);

        CourseAssignment assignment = new CourseAssignment();
        assignment.setId(1);
        assignment.setCourse(course);
        assignment.setStartDate(LocalDate.of(2024, 1, 15));
        assignment.setEndDate(LocalDate.of(2024, 2, 15));

        when(courseAssignmentService.getCourseAssignmentById(1)).thenReturn(Optional.of(assignment));

        mockMvc.perform(get("/api/course-assignments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.course.id").value(1))
                .andExpect(jsonPath("$.startDate").value("2024-01-15"));
    }

    @Test
    public void getAllAssignments_ShouldReturnList() throws Exception {
        CourseAssignment assignment1 = new CourseAssignment();
        assignment1.setId(1);
        assignment1.setStartDate(LocalDate.of(2024, 1, 15));

        CourseAssignment assignment2 = new CourseAssignment();
        assignment2.setId(2);
        assignment2.setStartDate(LocalDate.of(2024, 2, 15));

        when(courseAssignmentService.getAllCourseAssignments()).thenReturn(Arrays.asList(assignment1, assignment2));

        mockMvc.perform(get("/api/course-assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void updateAssignment_ShouldReturnUpdatedAssignment() throws Exception {
        Course course = new Course();
        course.setId(1);

        CourseAssignment assignment = new CourseAssignment();
        assignment.setId(1);
        assignment.setCourse(course);
        assignment.setStartDate(LocalDate.of(2024, 1, 20));
        assignment.setEndDate(LocalDate.of(2024, 2, 20));

        when(courseAssignmentService.getCourseAssignmentById(1)).thenReturn(Optional.of(assignment));
        when(courseAssignmentService.updateCourseAssignment(any(CourseAssignment.class))).thenReturn(assignment);

        mockMvc.perform(put("/api/course-assignments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"course\":{\"id\":1}," +
                        "\"startDate\":\"2024-01-20\"," +
                        "\"endDate\":\"2024-02-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value("2024-01-20"))
                .andExpect(jsonPath("$.endDate").value("2024-02-20"));
    }

    @Test
    public void deleteAssignment_ShouldReturn200WhenSuccessful() throws Exception {
        CourseAssignment assignment = new CourseAssignment();
        assignment.setId(1);

        when(courseAssignmentService.getCourseAssignmentById(1)).thenReturn(Optional.of(assignment));

        mockMvc.perform(delete("/api/course-assignments/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAssignment_ShouldReturn404WhenNotFound() throws Exception {
        when(courseAssignmentService.getCourseAssignmentById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/course-assignments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAssignment_ShouldValidateDates() throws Exception {
        mockMvc.perform(post("/api/course-assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"course\":{\"id\":1}," +
                        "\"startDate\":\"2024-02-15\"," +
                        "\"endDate\":\"2024-01-15\"}"))  // End date before start date
                .andExpect(status().isBadRequest());
    }
}