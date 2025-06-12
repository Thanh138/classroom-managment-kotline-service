package com.example.student.model

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(
    name = "classes",
    schema = "student_schema",
    indexes = [
        Index(name = "idx_class_teacher", columnList = "teacher_id"),
        Index(name = "idx_class_name", columnList = "name", unique = true)
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class Class(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "teacher_username", nullable = false, length = 50)
    @ColumnDefault("''")
    val teacherUsername: String? = "",

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Column(nullable = false)
    var isActive: Boolean = true
) {
    companion object {
        fun create(
            name: String,
            description: String? = null,
            teacherUsername: String? = null,
            isActive: Boolean = true
        ): Class {
            return Class(
                name = name,
                description = description,
                teacherUsername = teacherUsername,
                isActive = isActive
            )
        }
    }

    protected constructor() : this(
        name = "",
        description = null,
        teacherUsername = ""
    )
}
