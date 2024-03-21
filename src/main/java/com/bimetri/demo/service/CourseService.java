package com.bimetri.demo.service;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;
import com.bimetri.demo.exception.ResourceNotFoundException;
import com.bimetri.demo.exception.message.ErrorMessage;
import com.bimetri.demo.mapper.CourseMapper;
import com.bimetri.demo.repository.CourseRepository;
import com.bimetri.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public List<CourseResponseDto> getAll() {
        log.info("Fetching all course from the database.");

        List<Course> contactMessageList = this.courseRepository.findAll();

        return CourseMapper.toDTOList(contactMessageList);
    }

    public CourseResponseDto getById(Long id) throws ResourceNotFoundException {
        log.info("Fetching course with ID: {}", id);

        Course course = this.courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return CourseMapper.toDTO(course);
    }

    public void create(CourseRequestDto courseRequestDto) {
        log.info("Creating a new course.");

        this.courseRepository.save(CourseMapper.toEntity(courseRequestDto));
    }

    public void update(Long id, CourseRequestDto courseRequestDto) {
        log.info("Updating course with ID: {}", id);

        Course courseToUpdate = this.courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        courseToUpdate.setName(courseRequestDto.getName());

        this.courseRepository.save(courseToUpdate);
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        log.info("Deleting course with ID: {}", id);

        Course course = this.courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        this.courseRepository.deleteById(course.getId());
    }
}
