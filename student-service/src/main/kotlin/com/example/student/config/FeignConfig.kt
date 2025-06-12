package com.example.student.config

import com.example.student.utils.CustomErrorDecoder
import feign.Logger
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class FeignConfig {
    @Bean
    fun feignErrorDecoder(): ErrorDecoder = CustomErrorDecoder()

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication is JwtAuthenticationToken) {
                val token = authentication.token.tokenValue
                template.header("Authorization", "Bearer $token")
            }
        }
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}
