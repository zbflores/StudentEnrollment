package com.example.student_enrollment.controllers;

import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.services.CourseService;
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

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    public void createCourse_ShouldReturnCreatedCourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("John Doe");

        Course course = new Course();
        course.setName("Introduction to Programming");
        course.setDescription("Learn programming basics");
        course.setCredits(3);
        course.setType("REQUIRED");
        course.setStartDate(LocalDate.of(2024, 1, 15));
        course.setEndDate(LocalDate.of(2024, 5, 15));
        course.setTeacher(teacher);

        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Introduction to Programming\"," +
                        "\"description\":\"Learn programming basics\"," +
                        "\"credits\":3," +
                        "\"type\":\"REQUIRED\"," +
                        "\"startDate\":\"2024-01-15\"," +
                        "\"endDate\":\"2024-05-15\"," +
                        "\"teacher\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Introduction to Programming"))
                .andExpect(jsonPath("$.credits").value(3))
                .andExpect(jsonPath("$.teacher.id").value(1));
    }

    @Test
    public void getCourse_ShouldReturnCourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        
        Course course = new Course();
        course.setId(1);
        course.setName("Introduction to Programming");
        course.setTeacher(teacher);

        when(courseService.getCourseById(1)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Introduction to Programming"))
                .andExpect(jsonPath("$.teacher.id").value(1));
    }

    @Test
    public void getAllCourses_ShouldReturnList() throws Exception {
        Course course1 = new Course();
        course1.setName("Introduction to Programming");
        
        Course course2 = new Course();
        course2.setName("Advanced Programming");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Introduction to Programming"))
                .andExpect(jsonPath("$[1].name").value("Advanced Programming"));
    }

    @Test
    public void updateCourse_ShouldReturnUpdatedCourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);

        Course course = new Course();
        course.setId(1);
        course.setName("Updated Programming Course");
        course.setCredits(4);
        course.setTeacher(teacher);

        when(courseService.getCourseById(1)).thenReturn(Optional.of(course));
        when(courseService.updateCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Programming Course\"," +
                        "\"credits\":4," +
                        "\"teacher\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Programming Course"))
                .andExpect(jsonPath("$.credits").value(4));
    }

    @Test
    public void deleteCourse_ShouldReturn404WhenNotFound() throws Exception {
        when(courseService.getCourseById(999)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/courses/999"))
                .andExpect(status().isNotFound());
    }
}