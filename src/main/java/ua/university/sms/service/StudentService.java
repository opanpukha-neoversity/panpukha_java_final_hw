package ua.university.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.BadRequestException;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.StudentGpaResponse;
import ua.university.sms.model.dto.StudentRequest;
import ua.university.sms.model.dto.StudentResponse;
import ua.university.sms.model.dto.TranscriptResponse;
import ua.university.sms.model.entity.Enrollment;
import ua.university.sms.model.entity.Grade;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Student with this email already exists");
        }
        Student student = new Student(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.enrollmentYear(),
            request.status()
        );
        return toResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll(StudentStatus status, Integer year, String search) {
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim();
        return studentRepository.findByFilters(status, year, normalizedSearch).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return toResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudent(id);
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setEnrollmentYear(request.enrollmentYear());
        student.setStatus(request.status());
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsWithUnpaidCourses() {
        return studentRepository.findStudentsWithUnpaidCourses().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public TranscriptResponse getTranscript(Long id) {
        Student student = studentRepository.findDetailedById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));

        List<TranscriptResponse.TranscriptLine> lines = student.getEnrollments().stream()
            .map(enrollment -> new TranscriptResponse.TranscriptLine(
                enrollment.getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getCourse().getCredits(),
                enrollment.getSemester(),
                enrollment.getYear(),
                enrollment.getGrade(),
                enrollment.isPaid()
            ))
            .toList();

        return new TranscriptResponse(student.getId(), student.getFullName(), calculateGpa(student.getEnrollments()), lines);
    }

    @Transactional(readOnly = true)
    public List<StudentGpaResponse> getTopStudents(int limit) {
        if (limit < 1) {
            throw new BadRequestException("Limit must be positive");
        }
        return studentRepository.findAllWithEnrollments().stream()
            .map(student -> new StudentGpaResponse(student.getId(), student.getFullName(), calculateGpa(student.getEnrollments())))
            .filter(response -> response.gpa() > 0)
            .sorted(Comparator.comparingDouble(StudentGpaResponse::gpa).reversed())
            .limit(limit)
            .toList();
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    private double calculateGpa(List<Enrollment> enrollments) {
        double weightedPoints = 0.0;
        int totalCredits = 0;
        for (Enrollment enrollment : enrollments) {
            Grade grade = enrollment.getGrade();
            if (grade == null || !grade.affectsGpa()) {
                continue;
            }
            int credits = enrollment.getCourse().getCredits();
            weightedPoints += grade.points() * credits;
            totalCredits += credits;
        }
        return totalCredits == 0 ? 0.0 : Math.round((weightedPoints / totalCredits) * 100.0) / 100.0;
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            student.getEnrollmentYear(),
            student.getStatus()
        );
    }
}
