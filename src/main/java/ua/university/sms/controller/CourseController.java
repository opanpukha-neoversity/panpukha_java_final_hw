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
import ua.university.sms.model.dto.CourseRequest;
import ua.university.sms.model.dto.CourseResponse;
import ua.university.sms.service.CourseService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "CRUD operations and filters for courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.create(request);
        return ResponseEntity.created(URI.create("/api/courses/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Get courses with optional filters by teacherId and credits")
    public List<CourseResponse> findAll(
        @RequestParam(required = false) Long teacherId,
        @RequestParam(required = false) Integer credits
    ) {
        return courseService.findAll(teacherId, credits);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by id")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
