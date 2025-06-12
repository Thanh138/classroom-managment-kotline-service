package com.example.auth.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val name: String
) {
    companion object {
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val ROLE_STAFF = "ROLE_STAFF"
        const val ROLE_TEACHER = "ROLE_TEACHER"
        const val ROLE_STUDENT = "ROLE_STUDENT"
        const val ROLE_GUEST = "ROLE_GUEST"
    }
}
