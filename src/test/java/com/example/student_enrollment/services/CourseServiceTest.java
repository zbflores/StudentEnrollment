package com.example.student_enrollment.services;

import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.repositories.CourseRepository;
import com.example.student_enrollment.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher();
        testTeacher.setId(1);
        testTeacher.setName("John Doe");

        testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName("Java Programming");
        testCourse.setDescription("Learn Java basics");
        testCourse.setCredits(3);
        testCourse.setType("REQUIRED");
        testCourse.setStartDate(LocalDate.of(2024, 1, 15));
        testCourse.setEndDate(LocalDate.of(2024, 5, 15));
        testCourse.setTeacher(testTeacher);
    }

    @Test
    void getAllCourses_ShouldReturnList() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getName());
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseById_ShouldReturnCourse() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(testCourse));

        Optional<Course> result = courseService.getCourseById(1);

        assertTrue(result.isPresent());
        assertEquals("Java Programming", result.get().getName());
        assertEquals(3, result.get().getCredits());
        verify(courseRepository).findById(1);
    }

    @Test
    void createCourse_ShouldReturnSavedCourse() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(testTeacher));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.createCourse(testCourse);

        assertNotNull(result);
        assertEquals("Java Programming", result.getName());
        assertEquals(testTeacher.getId(), result.getTeacher().getId());
        verify(courseRepository).save(any(Course.class));
        verify(teacherRepository).findById(1);
    }

    @Test
    void createCourse_ShouldThrowException_WhenTeacherNotFound() {
        when(teacherRepository.findById(999)).thenReturn(Optional.empty());
        testCourse.getTeacher().setId(999);

        assertThrows(ResponseStatusException.class, () -> {
            courseService.createCourse(testCourse);
        });

        verify(teacherRepository).findById(999);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void updateCourse_ShouldReturnUpdatedCourse() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(testTeacher));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.updateCourse(testCourse);

        assertNotNull(result);
        assertEquals("Java Programming", result.getName());
        assertEquals(LocalDate.of(2024, 1, 15), result.getStartDate());
        verify(courseRepository).save(testCourse);
    }

    @Test
    void deleteCourse_ShouldCallRepository() {
        courseService.deleteCourse(1);
        verify(courseRepository).deleteById(1);
    }

    @Test
    void getCourseById_ShouldReturnEmpty_WhenNotFound() {
        when(courseRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.getCourseById(999);

        assertFalse(result.isPresent());
        verify(courseRepository).findById(999);
    }

    @Test
    void createCourse_ShouldHandleNullTeacher() {
        testCourse.setTeacher(null);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.createCourse(testCourse);

        assertNotNull(result);
        assertNull(result.getTeacher());
        verify(courseRepository).save(any(Course.class));
        verify(teacherRepository, never()).findById(any());
    }
}