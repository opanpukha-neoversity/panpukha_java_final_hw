package ua.university.sms.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(
    @NotBlank String name,
    @NotNull @Min(1) @Max(30) Integer credits,
    String description,
    @NotNull Long teacherId
) {
}
