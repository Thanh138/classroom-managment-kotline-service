package com.example.student.model.dto

import java.time.Instant

data class ClassDto(
    val id: Long,
    val name: String,
    val description: String?,
    val teacherId: Long,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)
