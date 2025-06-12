package com.example.student.model.request

import jakarta.validation.constraints.NotBlank

data class CreateClassRequest(
    @field:NotBlank(message = "Class name is required")
    val name: String,
    
    val description: String? = null,
    
    // Nullable for draft classes
    val teacherUsername: String? = null
)