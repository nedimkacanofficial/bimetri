package com.bimetri.demo.mapper;

import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentResponseDto toDTO(Student student) {
        StudentResponseDto responseDTO = new StudentResponseDto();

        responseDTO.setId(student.getId());
        responseDTO.setName(student.getName());
        responseDTO.setSurname(student.getSurname());
        responseDTO.setSchoolNumber(Long.valueOf(student.getSchoolNumber()));

        return responseDTO;
    }

    public static Student toEntity(StudentRequestDto requestDTO) {
        Student student = new Student();


        student.setName(requestDTO.getName());
        student.setSurname(requestDTO.getSurname());
        student.setSchoolNumber(requestDTO.getSchoolNumber());

        return student;
    }

    public static List<StudentResponseDto> toDTOList(List<Student> students) {
        return students.stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }
}
