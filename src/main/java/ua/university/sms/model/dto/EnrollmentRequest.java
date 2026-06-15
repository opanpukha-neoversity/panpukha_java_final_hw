package ua.university.sms.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull Long studentId,
    @NotNull Long courseId,
    @NotBlank String semester,
    @NotNull @Min(2000) @Max(2100) Integer year
) {
}
