package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;
import com.bimetri.demo.dto.response.StudentResponseDto;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.exception.message.ErrorMessage;
import com.bimetri.demo.mapper.CourseMapper;
import com.bimetri.demo.mapper.StudentMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public List<StudentResponseDto> getAll() {
        log.info("Fetching all student from the database.");

        List<Student> studentList = this.studentRepository.findAll();

        return StudentMapper.toDTOList(studentList);
    }

    public StudentResponseDto getById(Long id) throws ResourceNotFoundException {
        log.info("Fetching student with ID: {}", id);

        Student student = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return StudentMapper.toDTO(student);
    }

    public void create(StudentRequestDto studentRequestDto) {
        log.info("Creating a new student.");

        this.studentRepository.save(StudentMapper.toEntity(studentRequestDto));
    }

    public void update(Long id, StudentRequestDto studentRequestDto) {
        log.info("Updating student with ID: {}", id);

        Student studentToUpdate = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        studentToUpdate.setName(studentRequestDto.getName());
        studentToUpdate.setSurname(studentRequestDto.getSurname());
        studentToUpdate.setSchoolNumber(studentRequestDto.getSchoolNumber());
        studentToUpdate.setStudentClass(studentRequestDto.getStudentClass());

        this.studentRepository.save(studentToUpdate);
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        log.info("Deleting student with ID: {}", id);

        Student student = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        this.studentRepository.deleteById(student.getId());
    }
}
