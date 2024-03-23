package com.bimetri.demo.controller;

import com.bimetri.demo.dto.defaultResponse.DefaultResponseDTO;
import com.bimetri.demo.dto.defaultResponse.ResponseMessage;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;
import com.bimetri.demo.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    /**
     * Retrieves all courses.
     * <p>
     * This endpoint retrieves all courses available in the system.
     *
     * @return ResponseEntity containing a list of CourseResponseDto objects representing all courses,
     * along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAll() {
        log.info("Fetching all course.");

        List<CourseResponseDto> courseResponseDTO = this.courseService.getAll();

        return new ResponseEntity<>(courseResponseDTO, HttpStatus.OK);
    }

    /**
     * Creates a new course.
     * <p>
     * This endpoint creates a new course in the system based on the provided CourseRequestDto.
     *
     * @param courseRequestDto The CourseRequestDto containing the details of the course to be created.
     * @return ResponseEntity containing a DefaultResponseDTO indicating the success of the operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @PostMapping
    public ResponseEntity<DefaultResponseDTO> create(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        log.info("Creating a new course: {}", courseRequestDto.getName());

        this.courseService.create(courseRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.CREATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Updates an existing course.
     * <p>
     * This endpoint updates an existing course in the system with the provided ID,
     * based on the details provided in the CourseRequestDto.
     *
     * @param id               The ID of the course to be updated.
     * @param courseRequestDto The CourseRequestDto containing the updated details of the course.
     * @return ResponseEntity containing a DefaultResponseDTO indicating the success of the operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> update(@PathVariable long id, @Valid @RequestBody CourseRequestDto courseRequestDto) {
        log.info("Updating course with ID: {}", id);

        this.courseService.update(id, courseRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.UPDATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    /**
     * Deletes a course by its ID.
     * <p>
     * This endpoint deletes a course from the system based on the provided ID.
     *
     * @param id The ID of the course to be deleted.
     * @return ResponseEntity containing a DefaultResponseDTO indicating the success of the deletion operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> deleteById(@PathVariable long id) {
        log.info("Deleting course with ID: {}", id);

        this.courseService.deleteById(id);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.DELETED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    /**
     * Enrolls a student to a course by their IDs.
     * <p>
     * This endpoint enrolls a student to a course in the system based on the provided student ID and course ID.
     *
     * @param studentId The ID of the student to be enrolled.
     * @param courseId  The ID of the course to enroll the student into.
     * @return ResponseEntity containing a DefaultResponseDTO indicating the success of the enrollment operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @PostMapping("/enroll")
    public ResponseEntity<DefaultResponseDTO> enrollStudentToCourseByIds(@RequestParam Long studentId, @RequestParam Long courseId) {
        log.info("Enrool Student ID: {} and Course ID: {}", studentId, courseId);

        this.courseService.enrollStudentToCourse(studentId, courseId);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.CREATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all courses that currently have no students enrolled.
     * <p>
     * This endpoint retrieves a list of courses from the system that currently have no students enrolled.
     * These are courses where the student enrollment is null or empty.
     *
     * @return ResponseEntity containing a list of CourseResponseDto objects representing courses without students,
     * along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping("/courses-without-students")
    public ResponseEntity<List<CourseResponseDto>> findByStudentsIsNull() {
        log.info("Courses without students.");
        List<CourseResponseDto> courseResponseDtos = this.courseService.findByStudentsIsNull();

        return new ResponseEntity<>(courseResponseDtos, HttpStatus.OK);
    }

    /**
     * Retrieves all courses associated with a specific student.
     * <p>
     * This endpoint retrieves a list of courses associated with the student identified by the provided ID.
     * It returns the courses in which the specified student is enrolled.
     *
     * @param studentId The ID of the student for whom to retrieve the associated courses.
     * @return ResponseEntity containing a list of CourseResponseDto objects representing the courses associated
     * with the specified student, along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping("/students-all-courses/{studentId}")
    public ResponseEntity<List<CourseResponseDto>> findStudentsByStudentsId(@PathVariable long studentId) {
        log.info("All courses Student ID: {}", studentId);
        List<CourseResponseDto> courseResponseDtos = this.courseService.findStudentCourses(studentId);

        return new ResponseEntity<>(courseResponseDtos, HttpStatus.OK);
    }
}
