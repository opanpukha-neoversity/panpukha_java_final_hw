package ua.university.sms.model.dto;

import ua.university.sms.model.entity.Grade;

import java.util.List;

public record TranscriptResponse(
    Long studentId,
    String studentName,
    double gpa,
    List<TranscriptLine> courses
) {
    public record TranscriptLine(
        Long enrollmentId,
        Long courseId,
        String courseName,
        Integer credits,
        String semester,
        Integer year,
        Grade grade,
        boolean paid
    ) {
    }
}
