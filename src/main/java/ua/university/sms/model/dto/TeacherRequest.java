package ua.university.sms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import ua.university.sms.model.entity.TeacherPosition;

import java.time.LocalDate;

public record TeacherRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank @Email String email,
    @NotNull @Past LocalDate dateOfBirth,
    @NotNull TeacherPosition position
) {
}
