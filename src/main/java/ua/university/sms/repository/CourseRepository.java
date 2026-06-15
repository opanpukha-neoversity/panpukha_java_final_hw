package ua.university.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
        select c from Course c
        join fetch c.teacher t
        where (:teacherId is null or t.id = :teacherId)
          and (:credits is null or c.credits = :credits)
        order by c.id
        """)
    List<Course> findByFilters(@Param("teacherId") Long teacherId, @Param("credits") Integer credits);
}
