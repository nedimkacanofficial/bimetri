package com.bimetri.demo.controller;

import com.bimetri.demo.dto.defaultResponse.DefaultResponseDTO;
import com.bimetri.demo.dto.defaultResponse.ResponseMessage;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentResponseDto;
import com.bimetri.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAll() {
        log.info("Fetching all student.");

        List<StudentResponseDto> studentResponseDTO = this.studentService.getAll();

        return new ResponseEntity<>(studentResponseDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StudentResponseDto> getById(@PathVariable long id) {
        log.info("Fetching student with ID: {}", id);

        StudentResponseDto studentResponseDTO = this.studentService.getById(id);

        return new ResponseEntity<>(studentResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> create(@Valid @RequestBody StudentRequestDto studentRequestDto) {
        log.info("Creating a new student: {}", studentRequestDto.getName());

        this.studentService.create(studentRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.CREATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> update(@PathVariable long id, @Valid @RequestBody StudentRequestDto studentRequestDto) {
        log.info("Updating student with ID: {}", id);

        this.studentService.update(id, studentRequestDto);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.UPDATED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DefaultResponseDTO> deleteById(@PathVariable long id) {
        log.info("Deleting student with ID: {}", id);

        this.studentService.deleteById(id);

        DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO(true, ResponseMessage.DELETED_SUCCESS_RESPONSE_MESSAGE);

        return new ResponseEntity<>(defaultResponseDTO, HttpStatus.CREATED);
    }
}
