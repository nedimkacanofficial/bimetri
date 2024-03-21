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

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAll() {
        log.info("Fetching all course.");

        List<CourseResponseDto> courseResponseDTO = this.courseService.getAll();

        return new ResponseEntity<>(courseResponseDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CourseResponseDto> getById(@PathVariable long id) {
        log.info("Fetching course with ID: {}", id);

        CourseResponseDto contactMessageResponseDTO = this.courseService.getById(id);

        return new ResponseEntity<>(contactMessageResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> create(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        log.info("Creating a new course: {}", courseRequestDto.getName());

        this.courseService.create(courseRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.CREATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> update(@PathVariable long id, @Valid @RequestBody CourseRequestDto courseRequestDto) {
        log.info("Updating course with ID: {}", id);

        this.courseService.update(id, courseRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.UPDATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> deleteById(@PathVariable long id) {
        log.info("Deleting course with ID: {}", id);

        this.courseService.deleteById(id);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.DELETED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }
}
