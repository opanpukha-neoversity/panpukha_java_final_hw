package ua.university.sms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndCourseIdAndSemesterIgnoreCaseAndYear(Long studentId, Long courseId, String semester, Integer year);

    @Override
    @EntityGraph(attributePaths = {"student", "course"})
    List<Enrollment> findAll();

    @EntityGraph(attributePaths = {"student", "course"})
    @Query("select e from Enrollment e where e.id = :id")
    java.util.Optional<Enrollment> findDetailedById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"student", "course"})
    @Query("""
        select e from Enrollment e
        where e.grade <> ua.university.sms.model.entity.Grade.NA
          and (:courseId is null or e.course.id = :courseId)
          and (:semester is null or lower(e.semester) = lower(:semester))
        """)
    List<Enrollment> findGradedForGpa(@Param("courseId") Long courseId, @Param("semester") String semester);
}
