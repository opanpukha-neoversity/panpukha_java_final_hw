package ua.university.sms.model.dto;

import ua.university.sms.model.entity.Grade;

public record EnrollmentResponse(
    Long id,
    Long studentId,
    String studentName,
    Long courseId,
    String courseName,
    String semester,
    Integer year,
    boolean paid,
    Grade grade
) {
}
