package com.example.student.utils

import com.example.student.exception.ResourceNotFoundException
import feign.FeignException
import feign.Response
import feign.RetryableException
import feign.codec.ErrorDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import org.springframework.security.access.AccessDeniedException

class CustomErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        val status = response.status()
        val request = response.request()
        val message = "${request.httpMethod()} ${request.url()} failed with status $status: ${response.reason()}"
        
        return when (status) {
            404 -> ResourceNotFoundException("Resource not found: ${request.url()}")
            403 -> AccessDeniedException("Access denied for ${request.url()}")
            in 400..499 -> FeignException.errorStatus(methodKey, response)
            in 500..599 -> FeignException.errorStatus(methodKey, response).apply {
                // For 5xx errors, we'll let Feign handle retries with its default configuration
                // The error will be wrapped in a RetryableException by Feign's default decoder
            }
            else -> FeignException.errorStatus(methodKey, response)
        }
    }
}