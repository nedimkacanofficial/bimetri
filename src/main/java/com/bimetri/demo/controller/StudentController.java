package com.bimetri.demo.controller;

import com.bimetri.demo.dto.defaultResponse.DefaultResponseDTO;
import com.bimetri.demo.dto.defaultResponse.ResponseMessage;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentAndCoursesResponseDto;
import com.bimetri.demo.dto.response.StudentResponseDto;
import com.bimetri.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StudentController {
    private final StudentService studentService;

    /**
     * Retrieves all students.
     * <p>
     * This endpoint retrieves a list of all students available in the system.
     * It returns a list of StudentResponseDto objects containing information about each student.
     *
     * @return ResponseEntity containing a list of StudentResponseDto objects representing all students in the system,
     * along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAll() {
        log.info("Fetching all student.");

        List<StudentResponseDto> studentResponseDTO = this.studentService.getAll();

        return new ResponseEntity<>(studentResponseDTO, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all students along with their associated courses.
     * <p>
     * This method fetches all students from the database along with the courses
     * each student is enrolled in. It constructs a list of {@link StudentAndCoursesResponseDto}
     * objects, where each object contains information about a student along with a comma-separated
     * string of course names the student is enrolled in.
     *
     * @return ResponseEntity containing a list of {@link StudentAndCoursesResponseDto} objects representing
     * each student along with their associated courses. Returns HTTP status code 200 (OK) if the
     * operation is successful.
     */
    @GetMapping(path = "/student-and-courses")
    public ResponseEntity<List<StudentAndCoursesResponseDto>> getStudentAndCoursesList() {
        log.info("Fetching all student and student courses.");

        List<StudentAndCoursesResponseDto> studentAndCoursesResponseDtos = this.studentService.getStudentAndCoursesList();

        return new ResponseEntity<>(studentAndCoursesResponseDtos, HttpStatus.OK);
    }

    /**
     * Creates a new student.
     * <p>
     * This endpoint creates a new student in the system based on the provided student data.
     * It expects a StudentRequestDto object containing information about the new student.
     * Upon successful creation, it returns a DefaultResponseDTO object with a success message.
     *
     * @param studentRequestDto The StudentRequestDto object containing information about the new student.
     * @return ResponseEntity containing a DefaultResponseDTO object indicating the success of the operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @PostMapping
    public ResponseEntity<DefaultResponseDTO> create(@Valid @RequestBody StudentRequestDto studentRequestDto) {
        log.info("Creating a new student: {}", studentRequestDto.getName());

        this.studentService.create(studentRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.CREATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Updates an existing student.
     * <p>
     * This endpoint updates an existing student in the system with the provided ID.
     * It expects a StudentRequestDto object containing the updated information for the student.
     * Upon successful update, it returns a DefaultResponseDTO object with a success message.
     *
     * @param id                The ID of the student to be updated.
     * @param studentRequestDto The StudentRequestDto object containing the updated information for the student.
     * @return ResponseEntity containing a DefaultResponseDTO object indicating the success of the operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> update(@PathVariable long id, @Valid @RequestBody StudentRequestDto studentRequestDto) {
        log.info("Updating student with ID: {}", id);

        this.studentService.update(id, studentRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.UPDATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    /**
     * Deletes a student by ID.
     * <p>
     * This endpoint deletes a student from the system with the provided ID.
     * It expects the ID of the student to be deleted as a path variable.
     * Upon successful deletion, it returns a DefaultResponseDTO object with a success message.
     *
     * @param id The ID of the student to be deleted.
     * @return ResponseEntity containing a DefaultResponseDTO object indicating the success of the deletion operation,
     * along with an HTTP status code indicating the success of the operation.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> deleteById(@PathVariable long id) {
        log.info("Deleting student with ID: {}", id);

        this.studentService.deleteById(id);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.DELETED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    /**
     * Retrieves a list of students without any courses.
     * <p>
     * This endpoint retrieves a list of students who are not enrolled in any courses.
     * It returns a ResponseEntity containing a list of StudentResponseDto objects representing the students without courses,
     * along with an HTTP status code indicating the success of the operation.
     *
     * @return ResponseEntity containing a list of StudentResponseDto objects representing the students without courses,
     * along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping(path = "/students-without-courses")
    public ResponseEntity<List<StudentResponseDto>> findStudentsByCoursesIsNull() {
        log.info("Student without courses");

        List<StudentResponseDto> studentsWithNoCourses = this.studentService.findStudentsByCoursesIsNull();

        return new ResponseEntity<>(studentsWithNoCourses, HttpStatus.OK);
    }

    /**
     * Retrieves a list of students enrolled in a specific course.
     * <p>
     * This endpoint retrieves a list of students who are enrolled in the course identified by the given courseId.
     * It returns a ResponseEntity containing a list of StudentResponseDto objects representing the students enrolled in the course,
     * along with an HTTP status code indicating the success of the operation.
     *
     * @param courseId The ID of the course for which to retrieve the list of students.
     * @return ResponseEntity containing a list of StudentResponseDto objects representing the students enrolled in the course,
     * along with an HTTP status code indicating the success of the operation.
     */
    @GetMapping(path = "/course-all-students/{courseId}")
    public ResponseEntity<List<StudentResponseDto>> findCoursesByCoursesId(@PathVariable long courseId) {
        log.info("Course all students Course ID: {}", courseId);

        List<StudentResponseDto> studentResponseDtos = this.studentService.findCoursesStudent(courseId);

        return new ResponseEntity<>(studentResponseDtos, HttpStatus.OK);
    }
}
