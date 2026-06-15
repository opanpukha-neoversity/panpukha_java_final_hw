package ua.university.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.university.sms.model.dto.StudentGpaResponse;
import ua.university.sms.model.dto.StudentRequest;
import ua.university.sms.model.dto.StudentResponse;
import ua.university.sms.model.dto.TranscriptResponse;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.service.StudentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "CRUD, filtering, unpaid courses, top students and transcript")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Create a new student")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.create(request);
        return ResponseEntity.created(URI.create("/api/students/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Get students with optional filters by status, year and search text")
    public List<StudentResponse> findAll(
        @RequestParam(required = false) StudentStatus status,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) String search
    ) {
        return studentService.findAll(status, year, search);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by id")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unpaid")
    @Operation(summary = "Get students who have at least one unpaid enrollment")
    public List<StudentResponse> findStudentsWithUnpaidCourses() {
        return studentService.findStudentsWithUnpaidCourses();
    }

    @GetMapping("/top")
    @Operation(summary = "Get top-N students by GPA")
    public List<StudentGpaResponse> getTopStudents(@RequestParam(defaultValue = "5") int limit) {
        return studentService.getTopStudents(limit);
    }

    @GetMapping("/{id}/transcript")
    @Operation(summary = "Get student transcript with weighted GPA")
    public TranscriptResponse getTranscript(@PathVariable Long id) {
        return studentService.getTranscript(id);
    }
}
