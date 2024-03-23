package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;
import com.bimetri.demo.exception.ConflictException;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.exception.message.ErrorMessage;
import com.bimetri.demo.mapper.CourseMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bimetri.demo.dto.enums.ResponseEnum.COURSE;
import static com.bimetri.demo.dto.enums.ResponseEnum.STUDENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    /**
     * Retrieves all courses from the database.
     * <p>
     * This method fetches all courses stored in the database and returns them as a list of CourseResponseDto objects.
     * It logs an information message indicating the retrieval process.
     *
     * @return A list of CourseResponseDto objects representing all courses in the database.
     */
    public List<CourseResponseDto> getAll() {
        log.info("Fetching all course from the database.");

        List<Course> contactMessageList = this.courseRepository.findAll();

        return CourseMapper.toDTOList(contactMessageList);
    }

    /**
     * Retrieves the courses associated with a student.
     * <p>
     * This method retrieves the courses associated with a student identified by the provided student ID.
     * It first checks if a student with the given ID exists in the database. If the student is found,
     * the courses associated with that student are retrieved. If no courses are associated with the student,
     * a ResourceNotFoundException is thrown. If no student is found with the specified ID, a
     * ResourceNotFoundException is also thrown.
     *
     * @param studentId The unique identifier of the student whose courses are to be retrieved.
     * @return A list of CourseResponseDto objects representing the courses associated with the student.
     * @throws ResourceNotFoundException if no student is found with the specified ID or if no courses are associated
     *                                   with the student.
     */
    public List<CourseResponseDto> findStudentCourses(Long studentId) {
        log.info("Fetching course with Student ID: {}", studentId);

        Optional<Student> student = this.studentRepository.findById(studentId);

        if (student.isPresent()) {
            List<Course> courses = student.get().getCourses();
            if (courses == null || courses.isEmpty()) {
                throw new ResourceNotFoundException(String.format(ErrorMessage.NOT_FOUND_MESSAGE, COURSE));
            }
            return CourseMapper.toDTOList(courses);
        } else {
            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, studentId));
        }
    }

    /**
     * Retrieves the courses with no associated students.
     * <p>
     * This method retrieves the courses that do not have any associated students.
     * It queries the database for courses with no students associated with them and returns
     * a list of CourseResponseDto objects representing these courses.
     *
     * @return A list of CourseResponseDto objects representing the courses with no associated students.
     */
    public List<CourseResponseDto> findByStudentsIsNull() {
        log.info("Fetching course student not found!");

        return CourseMapper.toDTOList(this.courseRepository.findByStudentsIsNull());
    }

    /**
     * Creates a new course in the system.
     * <p>
     * This method is responsible for creating a new course based on the provided CourseRequestDto object.
     * It first checks if a course with the same name already exists in the database. If a course with the same name
     * is found, a ConflictException is thrown to indicate the duplication. Otherwise, the method proceeds to save
     * the new course by converting the CourseRequestDto object to an entity using the CourseMapper and then
     * persisting it into the database.
     *
     * @param courseRequestDto The CourseRequestDto object containing the information of the course to be created
     * @throws ConflictException Thrown if a course with the same name already exists in the database
     */
    public void create(CourseRequestDto courseRequestDto) {
        log.info("Creating a new course.");

        if (this.courseRepository.existsByName(courseRequestDto.getName())) {
            throw new ConflictException(String.format(ErrorMessage.DUPLICATE_NAME, courseRequestDto.getName()));
        }

        this.courseRepository.save(CourseMapper.toEntity(courseRequestDto));
    }


    /**
     * Updates an existing course.
     * <p>
     * This method updates an existing course with the provided ID using the information provided in the CourseRequestDto.
     * It first retrieves the course from the database based on the given ID. If the course does not exist, it throws a
     * ResourceNotFoundException. Then, it updates the name of the retrieved course with the name provided in the
     * CourseRequestDto and saves the changes to the database using the course repository.
     *
     * @param id               The ID of the course to be updated.
     * @param courseRequestDto The CourseRequestDto object containing the updated information for the course.
     * @throws ResourceNotFoundException If no course exists with the provided ID.
     */
    public void update(Long id, CourseRequestDto courseRequestDto) {
        log.info("Updating course with ID: {}", id);

        Course courseToUpdate = this.courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        courseToUpdate.setName(courseRequestDto.getName());

        this.courseRepository.save(courseToUpdate);
    }

    /**
     * Deletes a course by its ID.
     * <p>
     * This method deletes a course from the database based on the provided ID. It first retrieves the course from the
     * database using the given ID. If the course does not exist, it throws a ResourceNotFoundException. Then, it deletes
     * the retrieved course from the database using the course repository.
     *
     * @param id The ID of the course to be deleted.
     * @throws ResourceNotFoundException If no course exists with the provided ID.
     */
    public void deleteById(Long id) throws ResourceNotFoundException {
        log.info("Deleting course with ID: {}", id);

        Course course = this.courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        this.courseRepository.deleteById(course.getId());
    }

    /**
     * Enrolls a student to a course.
     * <p>
     * This method enrolls a student to a course based on the provided student ID and course ID. It first retrieves
     * the student and course from the database using their respective IDs. If either the student or the course does
     * not exist, it throws a ResourceNotFoundException. It then checks if the maximum number of courses or students
     * allowed per course is exceeded. If not, it adds the student to the course's list of students and vice versa,
     * and saves the changes to the database. If the student is already enrolled in the course, it throws a ConflictException.
     *
     * @param studentId The ID of the student to be enrolled.
     * @param courseId  The ID of the course to which the student will be enrolled.
     * @throws ResourceNotFoundException If the student or the course does not exist with the provided ID.
     * @throws ConflictException         If the maximum number of courses or students allowed per course is exceeded,
     *                                   or if the student is already enrolled in the course.
     */
    public void enrollStudentToCourse(Long studentId, Long courseId) {
        log.info("Fetching course with Student ID: {}, Course ID: {}", studentId, courseId);

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, studentId)));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, courseId)));

        if (!student.getCourses().contains(course)) {
            if (student.getCourses().size() < 5) {
                if (course.getStudents().size() < 50) {
                    course.getStudents().add(student);
                    studentRepository.save(student);
                    student.getCourses().add(course);
                    courseRepository.save(course);
                } else {
                    throw new ConflictException(String.format(ErrorMessage.RESOURCE_MAX_COUNT, STUDENT));
                }
            } else {
                throw new ConflictException(String.format(ErrorMessage.RESOURCE_MAX_COUNT, COURSE));
            }
        } else {
            throw new ConflictException(ErrorMessage.DUPLICATE_COURSE);
        }
    }
}
