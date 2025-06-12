package com.example.auth.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    val username: String,

    @Column(nullable = false, length = 100)
    var password: String,

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),

    @CreatedBy
    @Column(updatable = false)
    val createdBy: String? = null,

    @LastModifiedBy
    var lastModifiedBy: String? = null
) {
    fun hasRole(roleName: String): Boolean {
        return roles.any { it.name == roleName }
    }
    fun addRole(role: Role) {
        roles.add(role)
    }
    // For JPA
    protected constructor() : this(
        username = "",
        password = "",
        email = "",
        isActive = false
    )
}