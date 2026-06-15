package ua.university.sms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);

    @Query("""
        select s from Student s
        where (:status is null or s.status = :status)
          and (:year is null or s.enrollmentYear = :year)
          and (:search is null or lower(concat(s.firstName, ' ', s.lastName, ' ', s.email)) like lower(concat('%', :search, '%')))
        order by s.id
        """)
    List<Student> findByFilters(
        @Param("status") StudentStatus status,
        @Param("year") Integer year,
        @Param("search") String search
    );

    @Query("""
        select distinct s from Student s
        join s.enrollments e
        where e.paid = false
        order by s.id
        """)
    List<Student> findStudentsWithUnpaidCourses();

    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    @Query("select distinct s from Student s")
    List<Student> findAllWithEnrollments();

    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    @Query("select s from Student s where s.id = :id")
    Optional<Student> findDetailedById(@Param("id") Long id);
}
