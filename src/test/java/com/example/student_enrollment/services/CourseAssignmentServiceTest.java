package com.example.student_enrollment.services;

import com.example.student_enrollment.models.CourseAssignment;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.repositories.CourseAssignmentRepository;
import com.example.student_enrollment.repositories.CourseRepository;
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
public class CourseAssignmentServiceTest {

    @Mock
    private CourseAssignmentRepository courseAssignmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseAssignmentService courseAssignmentService;

    private CourseAssignment testAssignment;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName("Java Programming");

        testAssignment = new CourseAssignment();
        testAssignment.setId(1);
        testAssignment.setCourse(testCourse);
        testAssignment.setStartDate(LocalDate.of(2024, 1, 15));
        testAssignment.setEndDate(LocalDate.of(2024, 2, 15));
    }

    @Test
    void getAllCourseAssignments_ShouldReturnList() {
        when(courseAssignmentRepository.findAll()).thenReturn(Arrays.asList(testAssignment));

        List<CourseAssignment> result = courseAssignmentService.getAllCourseAssignments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2024, 1, 15), result.get(0).getStartDate());
        verify(courseAssignmentRepository).findAll();
    }

    @Test
    void getCourseAssignmentById_ShouldReturnAssignment() {
        when(courseAssignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));

        Optional<CourseAssignment> result = courseAssignmentService.getCourseAssignmentById(1);

        assertTrue(result.isPresent());
        assertEquals(LocalDate.of(2024, 1, 15), result.get().getStartDate());
        verify(courseAssignmentRepository).findById(1);
    }

    @Test
    void createCourseAssignment_ShouldReturnSavedAssignment() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(testCourse));
        when(courseAssignmentRepository.save(any(CourseAssignment.class))).thenReturn(testAssignment);

        CourseAssignment result = courseAssignmentService.createCourseAssignment(testAssignment);

        assertNotNull(result);
        assertEquals(testCourse.getId(), result.getCourse().getId());
        assertEquals(LocalDate.of(2024, 1, 15), result.getStartDate());
        verify(courseAssignmentRepository).save(any(CourseAssignment.class));
        verify(courseRepository).findById(1);
    }

    @Test
    void createCourseAssignment_ShouldThrowException_WhenCourseNotFound() {
        when(courseRepository.findById(999)).thenReturn(Optional.empty());
        testAssignment.getCourse().setId(999);

        assertThrows(ResponseStatusException.class, () -> {
            courseAssignmentService.createCourseAssignment(testAssignment);
        });

        verify(courseRepository).findById(999);
        verify(courseAssignmentRepository, never()).save(any(CourseAssignment.class));
    }

    @Test
    void updateCourseAssignment_ShouldReturnUpdatedAssignment() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(testCourse));
        when(courseAssignmentRepository.save(any(CourseAssignment.class))).thenReturn(testAssignment);

        CourseAssignment result = courseAssignmentService.updateCourseAssignment(testAssignment);

        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 1, 15), result.getStartDate());
        assertEquals(LocalDate.of(2024, 2, 15), result.getEndDate());
        verify(courseAssignmentRepository).save(testAssignment);
    }

    @Test
    void deleteCourseAssignment_ShouldCallRepository() {
        courseAssignmentService.deleteCourseAssignment(1);
        verify(courseAssignmentRepository).deleteById(1);
    }

    @Test
    void getCourseAssignmentById_ShouldReturnEmpty_WhenNotFound() {
        when(courseAssignmentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<CourseAssignment> result = courseAssignmentService.getCourseAssignmentById(999);

        assertFalse(result.isPresent());
        verify(courseAssignmentRepository).findById(999);
    }

    @Test
    void createCourseAssignment_ShouldHandleNullCourse() {
        testAssignment.setCourse(null);
        when(courseAssignmentRepository.save(any(CourseAssignment.class))).thenReturn(testAssignment);

        CourseAssignment result = courseAssignmentService.createCourseAssignment(testAssignment);

        assertNotNull(result);
        assertNull(result.getCourse());
        verify(courseAssignmentRepository).save(any(CourseAssignment.class));
        verify(courseRepository, never()).findById(any());
    }
}