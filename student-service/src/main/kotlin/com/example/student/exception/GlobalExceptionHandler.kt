package com.example.student.exception

import com.example.student.model.response.ErrorResponse
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.reasonPhrase,
                message = ex.message,
                path = request.getDescription(false).substring(4)
            )
        )

    @ExceptionHandler(AlreadyEnrolledException::class)
    fun handleAlreadyEnrolled(ex: AlreadyEnrolledException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                error = HttpStatus.CONFLICT.reasonPhrase,
                message = ex.message,
                path = request.getDescription(false).substring(4)
            )
        )

    @ExceptionHandler(ClassFullException::class)
    fun handleClassFull(ex: ClassFullException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.FORBIDDEN.value(),
                error = HttpStatus.FORBIDDEN.reasonPhrase,
                message = ex.message,
                path = request.getDescription(false).substring(4)
            )
        )

    @ExceptionHandler(ClassAlreadyExistsException::class)
    fun handleClassAlreadyExists(ex: ClassAlreadyExistsException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                error = "Class Already Exists",
                message = ex.message,
                path = request.getDescription(false).substring(4)
            )
        )
        
    @ExceptionHandler(FeignException.NotFound::class)
    fun handleFeignNotFound(ex: FeignException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                error = "Resource Not Found",
                message = "The requested resource was not found: ${ex.contentUTF8()}",
                path = request.getDescription(false).substring(4)
            )
        )
        
    @ExceptionHandler(FeignException::class)
    fun handleFeignException(ex: FeignException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Service Unavailable",
                message = "Error communicating with user service: ${ex.contentUTF8()}",
                path = request.getDescription(false).substring(4)
            )
        )
        
    @ExceptionHandler(UserServiceException::class)
    fun handleUserServiceException(ex: UserServiceException, request: WebRequest) =
        ResponseEntity.status(ex.statusCode).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = ex.statusCode,
                error = "User Service Error",
                message = ex.message,
                path = request.getDescription(false).substring(4)
            )
        )
}

class ResourceNotFoundException(message: String) : RuntimeException(message)
class AlreadyEnrolledException(message: String) : RuntimeException(message)
class ClassFullException(message: String) : RuntimeException(message)
class ClassAlreadyExistsException(message: String) : RuntimeException(message)