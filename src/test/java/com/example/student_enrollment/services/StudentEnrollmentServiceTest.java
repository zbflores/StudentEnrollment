package com.example.student_enrollment.services;

import com.example.student_enrollment.models.StudentEnrollment;
import com.example.student_enrollment.models.Student;
import com.example.student_enrollment.models.Course;
import com.example.student_enrollment.repositories.StudentEnrollmentRepository;
import com.example.student_enrollment.repositories.StudentRepository;
import com.example.student_enrollment.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentEnrollmentServiceTest {

    @Mock
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentEnrollmentService studentEnrollmentService;

    private StudentEnrollment testEnrollment;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1);

        testCourse = new Course();
        testCourse.setId(1);

        testEnrollment = new StudentEnrollment();
        testEnrollment.setId(1);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
    }

    @Test
    void getAllEnrollments_ShouldReturnList() {
        when(studentEnrollmentRepository.findAll()).thenReturn(Arrays.asList(testEnrollment));

        List<StudentEnrollment> result = studentEnrollmentService.getAllEnrollments();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(studentEnrollmentRepository).findAll();
    }

    @Test
    void getEnrollmentById_ShouldReturnEnrollment() {
        when(studentEnrollmentRepository.findById(1)).thenReturn(Optional.of(testEnrollment));

        Optional<StudentEnrollment> result = studentEnrollmentService.getEnrollmentById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(studentEnrollmentRepository).findById(1);
    }

    @Test
    void createEnrollment_ShouldReturnSavedEnrollment() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1)).thenReturn(Optional.of(testCourse));
        when(studentEnrollmentRepository.save(any(StudentEnrollment.class))).thenReturn(testEnrollment);

        StudentEnrollment result = studentEnrollmentService.createEnrollment(testEnrollment);

        assertNotNull(result);
        verify(studentEnrollmentRepository).save(any(StudentEnrollment.class));
        verify(studentRepository).findById(1);
        verify(courseRepository).findById(1);
    }

    @Test
    void createEnrollment_ShouldThrowException_WhenStudentNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentEnrollmentService.createEnrollment(testEnrollment);
        });

        verify(studentRepository).findById(1);
        verify(studentEnrollmentRepository, never()).save(any(StudentEnrollment.class));
    }

    @Test
    void createEnrollment_ShouldThrowException_WhenCourseNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentEnrollmentService.createEnrollment(testEnrollment);
        });

        verify(courseRepository).findById(1);
        verify(studentEnrollmentRepository, never()).save(any(StudentEnrollment.class));
    }

    @Test
    void updateEnrollment_ShouldReturnUpdatedEnrollment() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1)).thenReturn(Optional.of(testCourse));
        when(studentEnrollmentRepository.save(any(StudentEnrollment.class))).thenReturn(testEnrollment);

        StudentEnrollment result = studentEnrollmentService.updateEnrollment(testEnrollment);

        assertNotNull(result);
        verify(studentEnrollmentRepository).save(testEnrollment);
    }

    @Test
    void deleteEnrollment_ShouldCallRepository() {
        studentEnrollmentService.deleteEnrollment(1);
        verify(studentEnrollmentRepository).deleteById(1);
    }
}