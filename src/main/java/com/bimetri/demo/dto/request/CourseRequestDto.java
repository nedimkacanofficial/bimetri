package com.bimetri.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    @NotBlank(message = "Name is required.")
    @NotNull(message = "Name cannot be null.")
    @Size(min = 1, max = 50, message = "Your name must be between {min} and {max} characters.")
    private String name;

    @NotBlank(message = "Id is required.")
    @NotNull(message = "Id cannot be null.")
    private Long studentId;
}