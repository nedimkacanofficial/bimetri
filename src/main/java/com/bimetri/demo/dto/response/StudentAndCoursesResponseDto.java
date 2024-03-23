package com.bimetri.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAndCoursesResponseDto {
    private Long id;
    private String name;
    private String surname;
    private Long schoolNumber;
    private String courses;
}
