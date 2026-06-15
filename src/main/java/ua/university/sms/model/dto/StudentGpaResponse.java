package ua.university.sms.model.dto;

public record StudentGpaResponse(
    Long studentId,
    String studentName,
    double gpa
) {
}
