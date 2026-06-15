package ua.university.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.BadRequestException;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.EnrollmentRequest;
import ua.university.sms.model.dto.EnrollmentResponse;
import ua.university.sms.model.dto.GpaResponse;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Enrollment;
import ua.university.sms.model.entity.Grade;
import ua.university.sms.model.entity.Student;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.EnrollmentRepository;
import ua.university.sms.repository.StudentRepository;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
        EnrollmentRepository enrollmentRepository,
        StudentRepository studentRepository,
        CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndSemesterIgnoreCaseAndYear(
            request.studentId(), request.courseId(), request.semester(), request.year())) {
            throw new BadRequestException("Student is already enrolled in this course for the selected semester/year");
        }
        Student student = studentRepository.findById(request.studentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.courseId()));
        Enrollment enrollment = new Enrollment(student, course, request.semester(), request.year());
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findAll() {
        return enrollmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        return toResponse(findEnrollment(id));
    }

    @Transactional
    public EnrollmentResponse setGrade(Long id, Grade grade) {
        Enrollment enrollment = findEnrollment(id);
        enrollment.setGrade(grade);
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse markPaid(Long id) {
        Enrollment enrollment = findEnrollment(id);
        enrollment.markPaid();
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = findEnrollment(id);
        enrollmentRepository.delete(enrollment);
    }

    @Transactional(readOnly = true)
    public GpaResponse averageGpa(Long courseId, String semester) {
        String normalizedSemester = semester == null || semester.isBlank() ? null : semester.trim();
        List<Enrollment> enrollments = enrollmentRepository.findGradedForGpa(courseId, normalizedSemester);
        return new GpaResponse(calculateWeightedGpa(enrollments));
    }

    private Enrollment findEnrollment(Long id) {
        return enrollmentRepository.findDetailedById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + id));
    }

    private Double calculateWeightedGpa(List<Enrollment> enrollments) {
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
        if (totalCredits == 0) {
            return null;
        }
        return Math.round((weightedPoints / totalCredits) * 100.0) / 100.0;
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
            enrollment.getId(),
            enrollment.getStudent().getId(),
            enrollment.getStudent().getFullName(),
            enrollment.getCourse().getId(),
            enrollment.getCourse().getName(),
            enrollment.getSemester(),
            enrollment.getYear(),
            enrollment.isPaid(),
            enrollment.getGrade()
        );
    }
}
