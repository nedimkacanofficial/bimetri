package com.bimetri.demo.mapper;

import com.bimetri.demo.domain.Course;
import com.bimetri.demo.dto.request.CourseRequestDto;
import com.bimetri.demo.dto.response.CourseResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {
    public static CourseResponseDto toDTO(Course contactMessage) {
        CourseResponseDto responseDTO = new CourseResponseDto();

        responseDTO.setId(contactMessage.getId());
        responseDTO.setName(contactMessage.getName());

        return responseDTO;
    }

    public static Course toEntity(CourseRequestDto requestDTO) {
        Course course = new Course();

        course.setName(requestDTO.getName());

        return course;
    }

    public static List<CourseResponseDto> toDTOList(List<Course> courses) {
        return courses.stream().map(CourseMapper::toDTO).collect(Collectors.toList());
    }
}
