package ua.university.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.BadRequestException;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.TeacherRequest;
import ua.university.sms.model.dto.TeacherResponse;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.repository.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public TeacherResponse create(TeacherRequest request) {
        if (teacherRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Teacher with this email already exists");
        }
        Teacher teacher = new Teacher(request.firstName(), request.lastName(), request.email(), request.dateOfBirth(), request.position());
        return toResponse(teacherRepository.save(teacher));
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAll() {
        return teacherRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {
        return toResponse(findTeacher(id));
    }

    @Transactional
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = findTeacher(id);
        teacher.setFirstName(request.firstName());
        teacher.setLastName(request.lastName());
        teacher.setEmail(request.email());
        teacher.setDateOfBirth(request.dateOfBirth());
        teacher.setPosition(request.position());
        return toResponse(teacherRepository.save(teacher));
    }

    @Transactional
    public void delete(Long id) {
        Teacher teacher = findTeacher(id);
        teacherRepository.delete(teacher);
    }

    private Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
    }

    private TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(
            teacher.getId(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getDateOfBirth(),
            teacher.getPosition()
        );
    }
}
