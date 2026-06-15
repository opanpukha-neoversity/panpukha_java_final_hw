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
import ua.university.sms.model.dto.EnrollmentRequest;
import ua.university.sms.model.dto.EnrollmentResponse;
import ua.university.sms.model.dto.GpaResponse;
import ua.university.sms.model.dto.GradeRequest;
import ua.university.sms.service.EnrollmentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollments", description = "Course enrollment, grades, payments and GPA reports")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new enrollment. Initial grade is NA and paid is false")
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.create(request);
        return ResponseEntity.created(URI.create("/api/enrollments/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all enrollments")
    public List<EnrollmentResponse> findAll() {
        return enrollmentService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by id")
    public EnrollmentResponse getById(@PathVariable Long id) {
        return enrollmentService.getById(id);
    }

    @PutMapping("/{id}/grade")
    @Operation(summary = "Set grade for enrollment")
    public EnrollmentResponse setGrade(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        return enrollmentService.setGrade(id, request.grade());
    }

    @PutMapping("/{id}/paid")
    @Operation(summary = "Mark enrollment as paid")
    public EnrollmentResponse markPaid(@PathVariable Long id) {
        return enrollmentService.markPaid(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gpa")
    @Operation(summary = "Calculate average weighted GPA by course and/or semester")
    public GpaResponse averageGpa(
        @RequestParam(required = false) Long courseId,
        @RequestParam(required = false) String semester
    ) {
        return enrollmentService.averageGpa(courseId, semester);
    }
}
