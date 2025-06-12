package com.example.auth.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WellKnownController {

    @Value("\${server.port:9000}")
    private lateinit var serverPort: String

    @GetMapping("/.well-known/openid-configuration")
    fun openidConfiguration(): Map<String, Any> {
        val baseUrl = "http://localhost:$serverPort"

        return mapOf(
            "issuer" to baseUrl,
            "authorization_endpoint" to "$baseUrl/oauth2/authorize",
            "token_endpoint" to "$baseUrl/api/v1/auth/login",
            "jwks_uri" to "$baseUrl/oauth2/jwks",
            "userinfo_endpoint" to "$baseUrl/api/v1/auth/userinfo",
            "response_types_supported" to listOf("code", "token", "id_token"),
            "subject_types_supported" to listOf("public"),
            "id_token_signing_alg_values_supported" to listOf("RS256", "HS256"),
            "scopes_supported" to listOf("openid", "profile", "email"),
            "token_endpoint_auth_methods_supported" to listOf("client_secret_basic", "client_secret_post"),
            "claims_supported" to listOf(
                "sub", "iss", "aud", "exp", "iat", "auth_time",
                "name", "email", "email_verified", "preferred_username"
            )
        )
    }
}