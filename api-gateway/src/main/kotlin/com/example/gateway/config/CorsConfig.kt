package com.example.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono

@Configuration
class CorsConfig {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfig)
        }

        return CorsWebFilter(source)
    }

    @Bean
    fun corsHeaderFilter(): WebFilter {
        return WebFilter { exchange, chain ->
            val request = exchange.request
            val response = exchange.response
            val headers = response.headers

            // Add CORS headers to all responses
            headers.add("Access-Control-Allow-Origin", "*")
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            headers.add("Access-Control-Allow-Headers", "*")
            headers.add("Access-Control-Allow-Credentials", "true")
            headers.add("Access-Control-Max-Age", "3600")

            // Handle preflight requests
            if (request.method == HttpMethod.OPTIONS) {
                response.statusCode = HttpStatus.OK
                return@WebFilter Mono.empty<Void>()
            }

            chain.filter(exchange)
        }
    }
}