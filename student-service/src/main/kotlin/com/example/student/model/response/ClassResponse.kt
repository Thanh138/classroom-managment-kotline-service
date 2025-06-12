package com.example.student.model.response

import java.time.Instant

data class ClassResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val teacherUsername: String,
    val isActive: Boolean,
    val createdAt: Instant
)