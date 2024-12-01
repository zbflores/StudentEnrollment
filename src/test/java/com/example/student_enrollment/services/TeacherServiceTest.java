package com.example.student_enrollment.services;

import com.example.student_enrollment.models.Teacher;
import com.example.student_enrollment.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher();
        testTeacher.setId(1);
        testTeacher.setName("John Doe");
        testTeacher.setEmail("john.doe@school.com");
    }

    @Test
    void getAllTeachers_ShouldReturnList() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(testTeacher));

        // Act
        List<Teacher> result = teacherService.getAllTeachers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(teacherRepository).findAll();
    }

    @Test
    void getTeacherById_ShouldReturnTeacher() {
        // Arrange
        when(teacherRepository.findById(1)).thenReturn(Optional.of(testTeacher));

        // Act
        Optional<Teacher> result = teacherService.getTeacherById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(teacherRepository).findById(1);
    }

    @Test
    void createTeacher_ShouldReturnSavedTeacher() {
        // Arrange
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher result = teacherService.createTeacher(testTeacher);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void updateTeacher_ShouldReturnUpdatedTeacher() {
        // Arrange
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher result = teacherService.updateTeacher(testTeacher);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(teacherRepository).save(testTeacher);
    }

    @Test
    void deleteTeacher_ShouldCallRepository() {
        // Act
        teacherService.deleteTeacher(1);

        // Assert
        verify(teacherRepository).deleteById(1);
    }

    @Test
    void getTeacherById_ShouldReturnEmpty_WhenNotFound() {
        // Arrange
        when(teacherRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> result = teacherService.getTeacherById(999);

        // Assert
        assertFalse(result.isPresent());
        verify(teacherRepository).findById(999);
    }
}