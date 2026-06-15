package ua.university.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.university.sms.model.dto.EnrollmentRequest;
import ua.university.sms.model.dto.EnrollmentResponse;
import ua.university.sms.model.dto.GpaResponse;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Enrollment;
import ua.university.sms.model.entity.Grade;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.model.entity.TeacherPosition;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.EnrollmentRepository;
import ua.university.sms.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {
    @Mock
    EnrollmentRepository enrollmentRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    EnrollmentService enrollmentService;

    @Test
    void createShouldCreateEnrollmentWithDefaultGradeAndPayment() {
        Student student = student(1L);
        Course course = course(1L, 5);
        Enrollment saved = new Enrollment(student, course, "Spring", 2026);
        saved.setId(1L);

        when(enrollmentRepository.existsByStudentIdAndCourseIdAndSemesterIgnoreCaseAndYear(1L, 1L, "Spring", 2026)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);

        EnrollmentResponse response = enrollmentService.create(new EnrollmentRequest(1L, 1L, "Spring", 2026));

        assertThat(response.grade()).isEqualTo(Grade.NA);
        assertThat(response.paid()).isFalse();
    }

    @Test
    void setGradeShouldUpdateGrade() {
        Enrollment enrollment = new Enrollment(student(1L), course(1L, 5), "Spring", 2026);
        enrollment.setId(1L);
        when(enrollmentRepository.findDetailedById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        EnrollmentResponse response = enrollmentService.setGrade(1L, Grade.A);

        assertThat(response.grade()).isEqualTo(Grade.A);
    }

    @Test
    void markPaidShouldSetPaidTrue() {
        Enrollment enrollment = new Enrollment(student(1L), course(1L, 5), "Spring", 2026);
        enrollment.setId(1L);
        when(enrollmentRepository.findDetailedById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        EnrollmentResponse response = enrollmentService.markPaid(1L);

        assertThat(response.paid()).isTrue();
    }

    @Test
    void averageGpaShouldIgnoreNaGradesAndUseCredits() {
        Enrollment a = new Enrollment(student(1L), course(1L, 5), "Spring", 2026);
        a.setGrade(Grade.A);
        Enrollment c = new Enrollment(student(2L), course(2L, 10), "Spring", 2026);
        c.setGrade(Grade.C);
        Enrollment na = new Enrollment(student(3L), course(3L, 5), "Spring", 2026);
        na.setGrade(Grade.NA);

        when(enrollmentRepository.findGradedForGpa(null, null)).thenReturn(List.of(a, c, na));

        GpaResponse response = enrollmentService.averageGpa(null, null);

        assertThat(response.gpa()).isEqualTo(2.67);
    }

    private Student student(Long id) {
        Student student = new Student("Ivan", "Petrenko", "ivan" + id + "@example.com", 2026, StudentStatus.ACTIVE);
        student.setId(id);
        return student;
    }

    private Course course(Long id, int credits) {
        Teacher teacher = new Teacher("Olena", "Shevchenko", "teacher" + id + "@example.com", LocalDate.of(1985, 1, 1), TeacherPosition.LECTURER);
        teacher.setId(1L);
        Course course = new Course("Course " + id, credits, "Description", teacher);
        course.setId(id);
        return course;
    }
}
