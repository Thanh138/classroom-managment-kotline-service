package com.example.auth.repository

import com.example.auth.model.User
import com.example.auth.model.dto.UserDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun existsByIdAndRolesName(id: Long, role: String): Boolean
    
    @Query("""
        SELECT new com.example.auth.model.dto.UserDto(
            u.id, 
            u.username, 
            u.email, 
            (SELECT STRING_AGG(r.name, ',') FROM u.roles r), 
            u.isActive, 
            u.createdAt, 
            u.updatedAt
        ) 
        FROM User u 
        LEFT JOIN u.roles r
        WHERE 
            (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) 
            OR (LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:role IS NULL OR r.name = :role)
        GROUP BY u.id, u.username, u.email, u.isActive, u.createdAt, u.updatedAt
    """)
    fun searchUsers(
        @Param("query") query: String, 
        @Param("role") role: String?, 
        pageable: Pageable
    ): Page<UserDto>
}