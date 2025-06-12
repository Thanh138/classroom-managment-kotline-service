package com.example.student.service

import com.example.student.client.UserClient
import com.example.student.exception.ClassAlreadyExistsException
import com.example.student.exception.ResourceNotFoundException
import com.example.student.exception.UserServiceException
import com.example.student.model.Class
import com.example.student.model.request.CreateClassRequest
import com.example.student.model.response.ClassResponse
import com.example.student.repository.ClassRepository
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClassService(
    private val classRepository: ClassRepository,
    private val userClient: UserClient
) {
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    fun createClass(request: CreateClassRequest, authenticatedUsername: String): ClassResponse {
        try {
            val teacherUsername = request.teacherUsername
            teacherUsername?.let { username ->
                if (classRepository.existsByNameAndTeacherUsername(request.name, username)) {
                    throw ClassAlreadyExistsException("Class with name '${request.name}' already exists for this teacher")
                }
                try {
                    val teacher = userClient.getUserByUsername(username)
                        ?: throw UserServiceException("Teacher with username $username not found", 
                            HttpStatus.NOT_FOUND.value())

                    if (!userClient.userHasRole(teacher.id, "TEACHER")) {
                        throw UserServiceException("User ${teacher.username} is not a teacher", 
                            HttpStatus.BAD_REQUEST.value())
                    }
                } catch (ex: FeignException.NotFound) {
                    throw UserServiceException("Teacher with username $username not found", 
                        HttpStatus.NOT_FOUND.value(), ex)
                } catch (ex: FeignException) {
                    throw UserServiceException("Error validating teacher: ${ex.message}", 
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), ex)
                }
            }
            val newClass = Class.create(
                name = request.name,
                description = request.description,
                teacherUsername = teacherUsername,
                isActive = teacherUsername != null
            )

            val savedClass = classRepository.save(newClass)
            return savedClass.toResponse()
        } catch (ex: UserServiceException) {
            throw ex
        } catch (ex: Exception) {
            throw UserServiceException("Failed to create class: ${ex.message}", 
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ex)
        }
    }


    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    fun getClassById(classId: Long): ClassResponse {
        val classEntity = classRepository.findById(classId)
            .orElseThrow { ResourceNotFoundException("Class not found with id: $classId") }

        return classEntity.toResponse()
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    fun getClassesByTeacher(teacherUsername: String): List<ClassResponse> {
        return classRepository.findByTeacherUsername(teacherUsername).map { it.toResponse() }
    }

    private fun Class.toResponse() = ClassResponse(
        id = id,
        name = name,
        description = description,
        teacherUsername = teacherUsername ?: "",
        isActive = isActive,
        createdAt = createdAt
    )
}