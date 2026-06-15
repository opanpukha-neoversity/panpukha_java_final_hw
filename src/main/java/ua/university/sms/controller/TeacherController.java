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
import org.springframework.web.bind.annotation.RestController;
import ua.university.sms.model.dto.TeacherRequest;
import ua.university.sms.model.dto.TeacherResponse;
import ua.university.sms.service.TeacherService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Teachers", description = "CRUD operations for teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    @Operation(summary = "Create a new teacher")
    public ResponseEntity<TeacherResponse> create(@Valid @RequestBody TeacherRequest request) {
        TeacherResponse response = teacherService.create(request);
        return ResponseEntity.created(URI.create("/api/teachers/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all teachers")
    public List<TeacherResponse> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by id")
    public TeacherResponse getById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher")
    public TeacherResponse update(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        return teacherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
