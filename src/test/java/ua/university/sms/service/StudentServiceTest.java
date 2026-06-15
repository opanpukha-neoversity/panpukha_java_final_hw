package ua.university.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.StudentRequest;
import ua.university.sms.model.dto.StudentResponse;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.repository.StudentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentService studentService;

    @Test
    void createShouldSaveStudent() {
        StudentRequest request = new StudentRequest("Ivan", "Petrenko", "ivan@example.com", 2026, StudentStatus.ACTIVE);
        Student saved = new Student("Ivan", "Petrenko", "ivan@example.com", 2026, StudentStatus.ACTIVE);
        saved.setId(1L);

        when(studentRepository.existsByEmail("ivan@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        StudentResponse response = studentService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("ivan@example.com");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getByIdShouldReturnStudent() {
        Student student = new Student("Ivan", "Petrenko", "ivan@example.com", 2026, StudentStatus.ACTIVE);
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getById(1L);

        assertThat(response.firstName()).isEqualTo("Ivan");
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Student not found");
    }
}
