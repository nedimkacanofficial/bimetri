package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;
import com.bimetri.demo.exception.ConflictException;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.mapper.CourseMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseResponseDto> result = courseService.getAll();

        assertEquals(courses.size(), result.size());
    }

    @Test
    void testFindStudentCourses() {
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        student.setCourses(courses);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        List<CourseResponseDto> result = courseService.findStudentCourses(studentId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindStudentCourses_NotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.findStudentCourses(studentId));
    }

    @Test
    void testCreate() {
        CourseRequestDto courseRequestDto = new CourseRequestDto();
        courseRequestDto.setName("Test Course");
        Course course = CourseMapper.toEntity(courseRequestDto);
        when(courseRepository.existsByName(courseRequestDto.getName())).thenReturn(false);
        when(courseRepository.save(course)).thenReturn(course);

        assertDoesNotThrow(() -> courseService.create(courseRequestDto));
    }

    @Test
    void testCreate_DuplicateName() {
        CourseRequestDto courseRequestDto = new CourseRequestDto();
        courseRequestDto.setName("Test Course");
        when(courseRepository.existsByName(courseRequestDto.getName())).thenReturn(true);

        assertThrows(ConflictException.class, () -> courseService.create(courseRequestDto));
    }

    @Test
    void testEnrollStudentToCourse() {
        Long studentId = 1L;
        Long courseId = 1L;

        Student student = new Student();
        student.setId(studentId);
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }

        Course course = new Course();
        course.setId(courseId);
        if (course.getStudents() == null) {
            course.setStudents(new ArrayList<>());
        }

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);
        when(studentRepository.save(student)).thenReturn(student);
        assertDoesNotThrow(() -> courseService.enrollStudentToCourse(studentId, courseId));

        assertTrue(student.getCourses().contains(course));
    }

    @Test
    void testEnrollStudentToCourse_StudentNotFound() {
        Long studentId = 1L;
        Long courseId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.enrollStudentToCourse(studentId, courseId));
    }

    @Test
    void testEnrollStudentToCourse_CourseNotFound() {
        Long studentId = 1L;
        Long courseId = 1L;
        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.enrollStudentToCourse(studentId, courseId));
    }
}
