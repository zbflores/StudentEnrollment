package com.example.student_enrollment.services;

import com.example.student_enrollment.models.Student;
import com.example.student_enrollment.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setName("Jane Smith");
        testStudent.setEmail("jane.smith@student.com");
        testStudent.setBirthDate(LocalDate.of(2000, 1, 15));
        testStudent.setGradeLevel("FRESHMAN");
    }

    @Test
    void getAllStudents_ShouldReturnList() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(testStudent));

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane Smith", result.get(0).getName());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_ShouldReturnStudent() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));

        Optional<Student> result = studentService.getStudentById(1);

        assertTrue(result.isPresent());
        assertEquals("Jane Smith", result.get().getName());
        verify(studentRepository).findById(1);
    }

    @Test
    void createStudent_ShouldReturnSavedStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.createStudent(testStudent);

        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        assertEquals("FRESHMAN", result.getGradeLevel());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.updateStudent(testStudent);

        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        assertEquals(LocalDate.of(2000, 1, 15), result.getBirthDate());
        verify(studentRepository).save(testStudent);
    }

    @Test
    void deleteStudent_ShouldCallRepository() {
        studentService.deleteStudent(1);
        verify(studentRepository).deleteById(1);
    }

    @Test
    void getStudentById_ShouldReturnEmpty_WhenNotFound() {
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.getStudentById(999);

        assertFalse(result.isPresent());
        verify(studentRepository).findById(999);
    }
}