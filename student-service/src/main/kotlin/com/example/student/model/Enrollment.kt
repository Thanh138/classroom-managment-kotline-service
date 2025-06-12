package com.example.student.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "enrollments",
    schema = "student_schema",
    indexes = [
        Index(name = "idx_enrollment_student", columnList = "student_id"),
        Index(name = "idx_enrollment_class", columnList = "class_id")
    ]
)
data class Enrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "class_id", nullable = false)
    val classId: Long,

    @Column(name = "enrolled_by", nullable = false)
    val enrolledById: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: EnrollmentStatus = EnrollmentStatus.ACTIVE,
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    val enrolledAt: Instant = Instant.now(),
    @Column
    var completedAt: Instant? = null
) {
    companion object {
        fun create(
            studentId: Long,
            classId: Long,
            enrolledById: Long,
            status: EnrollmentStatus = EnrollmentStatus.ACTIVE
        ): Enrollment {
            return Enrollment(
                studentId = studentId,
                classId = classId,
                enrolledById = enrolledById,
                status = status
            )
        }
    }
    enum class EnrollmentStatus {
        ACTIVE,
        COMPLETED,
        DROPPED
    }
    // For JPA
    @Suppress("unused")
    protected constructor() : this(
        studentId = 0,
        classId = 0,
        enrolledById = 0
    )
}
