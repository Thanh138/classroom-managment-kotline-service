package com.example.auth.exception

class ResourceNotFoundException(
    message: String,
    val resourceName: String? = null,
    val fieldName: String? = null,
    val fieldValue: Any? = null
) : RuntimeException("$message. Resource: $resourceName, Field: $fieldName, Value: $fieldValue")