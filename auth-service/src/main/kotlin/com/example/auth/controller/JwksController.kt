package com.example.auth.controller

import com.example.auth.utils.JwtUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JwksController(
    private val jwtUtils: JwtUtils
) {

    @GetMapping("/oauth2/jwks")
    fun jwks(): Map<String, Any> {
        return mapOf(
            "keys" to listOf(
                mapOf(
                    "kty" to "oct",
                    "use" to "sig",
                    "kid" to "auth-service-key",
                    "alg" to "HS256"
                )
            )
        )
    }
}