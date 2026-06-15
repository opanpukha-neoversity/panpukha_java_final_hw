package ua.university.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.CourseRequest;
import ua.university.sms.model.dto.CourseResponse;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.TeacherRepository;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Teacher teacher = findTeacher(request.teacherId());
        Course course = new Course(request.name(), request.credits(), request.description(), teacher);
        return toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> findAll(Long teacherId, Integer credits) {
        return courseRepository.findByFilters(teacherId, credits).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return toResponse(findCourse(id));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourse(id);
        Teacher teacher = findTeacher(request.teacherId());
        course.setName(request.name());
        course.setCredits(request.credits());
        course.setDescription(request.description());
        course.setTeacher(teacher);
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        Course course = findCourse(id);
        courseRepository.delete(course);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
    }

    private Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getName(),
            course.getCredits(),
            course.getDescription(),
            course.getTeacher().getId(),
            course.getTeacher().getFullName()
        );
    }
}
