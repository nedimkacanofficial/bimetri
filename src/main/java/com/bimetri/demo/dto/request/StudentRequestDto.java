package com.bimetri.demo.dto.request;

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
public class StudentRequestDto {
    @NotBlank(message = "Name is required.")
    @NotNull(message = "Name cannot be null.")
    @Size(min = 1, max = 50, message = "Your name must be between {min} and {max} characters.")
    private String name;

    @NotBlank(message = "Surname is required.")
    @NotNull(message = "Surname cannot be null.")
    @Size(min = 1, max = 50, message = "Your surname must be between {min} and {max} characters.")
    private String surname;

    @NotBlank(message = "School Number is required.")
    @NotNull(message = "School Number cannot be null.")
    @Size(min = 3, max = 50, message = "Your school number must be between {min} and {max} characters.")
    private String schoolNumber;
}
