package ua.university.sms.model.dto;

import jakarta.validation.constraints.NotNull;
import ua.university.sms.model.entity.Grade;

public record GradeRequest(
    @NotNull Grade grade
) {
}
