package com.bimetri.demo.mapper;

import com.bimetri.demo.domain.Student;
import com.bimetri.demo.dto.request.StudentRequestDto;
import com.bimetri.demo.dto.response.StudentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentResponseDto toDTO(Student contactMessage) {
        StudentResponseDto responseDTO = new StudentResponseDto();

        responseDTO.setId(contactMessage.getId());
        responseDTO.setName(contactMessage.getName());

        return responseDTO;
    }

    public static Student toEntity(StudentRequestDto requestDTO) {
        Student student = new Student();


        student.setName(requestDTO.getName());
        student.setSurname(requestDTO.getSurname());
        student.setSchoolNumber(requestDTO.getSchoolNumber());
        student.setStudentClass(requestDTO.getStudentClass());

        return student;
    }

    public static List<StudentResponseDto> toDTOList(List<Student> students) {
        return students.stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }
}
