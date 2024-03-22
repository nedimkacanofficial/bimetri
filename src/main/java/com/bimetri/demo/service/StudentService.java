package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentResponseDto;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.exception.message.ErrorMessage;
import com.bimetri.demo.mapper.StudentMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bimetri.demo.dto.enums.ResponseEnum.STUDENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /**
     * Retrieves all students from the database.
     * <p>
     * This method fetches all students stored in the database and maps them to their corresponding Data Transfer Objects (DTOs).
     * It then returns a list of StudentResponseDto containing the information of all retrieved students.
     *
     * @return A list of StudentResponseDto containing information about all students.
     */
    public List<StudentResponseDto> getAll() {
        log.info("Fetching all student from the database.");

        List<Student> students = this.studentRepository.findAll();

        return StudentMapper.toDTOList(students);
    }

    /**
     * Retrieves a student by their ID.
     * <p>
     * This method fetches a student from the database based on the provided ID.
     * If a student with the specified ID is found, it is mapped to a StudentResponseDto object
     * and returned. Otherwise, a ResourceNotFoundException is thrown.
     *
     * @param id The ID of the student to retrieve.
     * @return A StudentResponseDto containing information about the retrieved student.
     * @throws ResourceNotFoundException if no student with the given ID is found in the database.
     */
    public StudentResponseDto getById(Long id) {
        log.info("Fetching student with ID: {}", id);

        Student student = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return StudentMapper.toDTO(student);
    }

    /**
     * Creates a new student.
     * <p>
     * This method creates a new student entity in the database based on the information provided
     * in the StudentRequestDto object.
     *
     * @param studentRequestDto The StudentRequestDto containing information about the student to be created.
     */
    public void create(StudentRequestDto studentRequestDto) {
        log.info("Creating a new student.");

        this.studentRepository.save(StudentMapper.toEntity(studentRequestDto));
    }

    /**
     * Updates an existing student.
     * <p>
     * This method updates the information of an existing student in the database with the provided ID.
     * It retrieves the student entity by the given ID, updates its information based on the fields in
     * the StudentRequestDto object, and then saves the updated entity back to the database.
     *
     * @param id                The ID of the student to be updated.
     * @param studentRequestDto The StudentRequestDto containing the updated information for the student.
     * @throws ResourceNotFoundException if no student with the specified ID is found in the database.
     */
    public void update(Long id, StudentRequestDto studentRequestDto) {
        log.info("Updating student with ID: {}", id);

        Student studentToUpdate = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        studentToUpdate.setName(studentRequestDto.getName());
        studentToUpdate.setSurname(studentRequestDto.getSurname());
        studentToUpdate.setSchoolNumber(studentRequestDto.getSchoolNumber());

        this.studentRepository.save(studentToUpdate);
    }

    /**
     * Deletes a student by ID.
     * <p>
     * This method deletes a student from the database based on the provided ID.
     * It retrieves the student entity by the given ID and then deletes it from the database.
     *
     * @param id The ID of the student to be deleted.
     * @throws ResourceNotFoundException if no student with the specified ID is found in the database.
     */
    public void deleteById(Long id) throws ResourceNotFoundException {
        log.info("Deleting student with ID: {}", id);

        Student student = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        this.studentRepository.deleteById(student.getId());
    }

    /**
     * Retrieves students who are not enrolled in any courses.
     * <p>
     * This method retrieves a list of students who are not enrolled in any courses from the database.
     * It queries the student repository to find students whose courses list is empty or null,
     * and then maps the resulting list of student entities to a list of StudentResponseDto objects.
     *
     * @return A list of StudentResponseDto objects representing students not enrolled in any courses.
     */
    public List<StudentResponseDto> findStudentsByCoursesIsNull() {
        log.info("Fetching courses student not found!");

        return StudentMapper.toDTOList(this.studentRepository.findByCoursesIsNull());
    }

    /**
     * Retrieves students enrolled in a specific course.
     * <p>
     * This method retrieves a list of students who are enrolled in the course identified by the given courseId.
     * It queries the course repository to find the course by its ID, and then retrieves the list of students
     * associated with that course. If the course is found and contains students, it maps the list of student entities
     * to a list of StudentResponseDto objects.
     *
     * @param courseId The ID of the course to retrieve students from.
     * @return A list of StudentResponseDto objects representing students enrolled in the specified course.
     * @throws ResourceNotFoundException if the course with the given ID is not found or if no students are enrolled in the course.
     */
    public List<StudentResponseDto> findCoursesStudent(long courseId) {
        log.info("Fetching course with student.");
        Optional<Course> course = this.courseRepository.findById(courseId);

        if (course.isPresent()) {
            List<Student> students = course.get().getStudents();
            if (students == null || students.isEmpty()) {
                throw new ResourceNotFoundException(String.format(ErrorMessage.NOT_FOUND_MESSAGE, STUDENT));
            }
            return StudentMapper.toDTOList(students);
        } else {
            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, courseId));
        }
    }
}
