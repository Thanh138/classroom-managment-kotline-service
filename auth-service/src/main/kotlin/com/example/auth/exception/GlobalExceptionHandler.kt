package com.example.auth.exception

import com.example.auth.model.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun handleNoHandlerFoundException(ex: NoHandlerFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = "Not Found",
            path = ex.requestURL
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}
