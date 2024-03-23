package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentAndCoursesResponseDto;
import com.bimetri.demo.dto.response.StudentResponseDto;
import com.bimetri.demo.exception.ConflictException;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.exception.message.ErrorMessage;
import com.bimetri.demo.mapper.StudentMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * Retrieves a list of all students along with their associated courses.
     * <p>
     * This method queries the database to fetch all students and their respective courses. It constructs
     * a list of {@link StudentAndCoursesResponseDto} objects, where each object contains information
     * about a student, including their ID, name, surname, and school number, along with a comma-separated
     * string of course names the student is enrolled in.
     *
     * @return A list of {@link StudentAndCoursesResponseDto} objects representing each student along with
     * their associated courses.
     */
    public List<StudentAndCoursesResponseDto> getStudentAndCoursesList() {
        log.info("Fetching all students and all student courses from the database.");

        List<Student> students = this.studentRepository.findAll();

        List<StudentAndCoursesResponseDto> studentAndCoursesList = new ArrayList<>();

        for (Student student : students) {
            StudentAndCoursesResponseDto studentAndCoursesResponseDto = new StudentAndCoursesResponseDto();
            studentAndCoursesResponseDto.setId(student.getId());
            studentAndCoursesResponseDto.setName(student.getName());
            studentAndCoursesResponseDto.setSurname(student.getSurname());
            studentAndCoursesResponseDto.setSchoolNumber(Long.valueOf(student.getSchoolNumber()));

            List<String> courseNames = new ArrayList<>();
            for (Course course : student.getCourses()) {
                courseNames.add(course.getName());
            }

            String coursesAsString = String.join(", ", courseNames);

            studentAndCoursesResponseDto.setCourses(coursesAsString);

            studentAndCoursesList.add(studentAndCoursesResponseDto);
        }

        return studentAndCoursesList;
    }

    /**
     * Creates a new student in the system.
     * <p>
     * This method is responsible for creating a new student based on the provided StudentRequestDto object.
     * It first checks if a student with the same school number already exists in the database. If a student with
     * the same school number is found, a ConflictException is thrown to indicate the duplication. Otherwise,
     * the method proceeds to save the new student by converting the StudentRequestDto object to an entity using
     * the StudentMapper and then persisting it into the database.
     *
     * @param studentRequestDto The StudentRequestDto object containing the information of the student to be created
     * @throws ConflictException Thrown if a student with the same school number already exists in the database
     */
    public void create(StudentRequestDto studentRequestDto) {
        log.info("Creating a new student.");

        if (this.studentRepository.existsBySchoolNumber(studentRequestDto.getSchoolNumber())) {
            throw new ConflictException(String.format(ErrorMessage.DUPLICATE_SCHOOL_NUMBER, studentRequestDto.getSchoolNumber()));
        }

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
