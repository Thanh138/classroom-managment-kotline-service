package com.example.auth.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.example.auth.model.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {
    private val objectMapper = ObjectMapper()

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        
        val errorResponse = ErrorResponse(
            status = HttpServletResponse.SC_UNAUTHORIZED,
            error = authException.javaClass.simpleName,
            message = authException.message ?: "Unauthorized",
            path = request.requestURI
        )
        
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}