package ua.university.sms.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ua.university.sms.model.interfaces.Payable;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_enrollment_student_course_semester_year",
        columnNames = {"student_id", "course_id", "semester", "year"}
    )
)
public class Enrollment implements Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 40)
    private String semester;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private boolean paid = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Grade grade = Grade.NA;

    public Enrollment() {
    }

    public Enrollment(Student student, Course course, String semester, Integer year) {
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.year = year;
        this.paid = false;
        this.grade = Grade.NA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public void markPaid() {
        this.paid = true;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
