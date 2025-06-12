package com.example.gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import java.net.URL
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .cors { } // Enable CORS configuration from WebConfig
            .authorizeExchange { exchanges ->
                exchanges
                    // Public endpoints - no authentication required
                    .pathMatchers(
                        "/api/v1/auth/login",
                        "/api/v1/auth/register",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/swagger-ui/**",
                        "/actuator/health"
                    ).permitAll()

                    // Protected auth endpoints - authentication required
                    .pathMatchers("/api/v1/auth/me").authenticated()

                    // Student service endpoints - require authentication
                    .pathMatchers("/api/v1/classes/**").authenticated()

                    
                    // Default: deny all other requests
                    .anyExchange().denyAll()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtDecoder(jwtDecoder())
                }
            }
            .build()
    }

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        val secretKey = SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
            .build()
    }

    // CORS is configured in WebConfig
}