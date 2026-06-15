package ua.university.sms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.university.sms.model.entity.StudentStatus;

public record StudentRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank @Email String email,
    @NotNull @Min(2000) @Max(2100) Integer enrollmentYear,
    @NotNull StudentStatus status
) {
}
