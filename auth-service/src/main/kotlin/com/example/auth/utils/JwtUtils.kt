package com.example.auth.utils

import com.example.auth.service.imp.UserDetailsImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
    @Value("\${jwt.expiration}")
    private val jwtExpirationMs: Long
) {
    private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateJwtToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        val email = if (userDetails is UserDetailsImpl) {
            userDetails.email
        } else {
            ""
        }

        val authorities = userDetails.authorities.map { it.authority }

        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("email", email)
            .claim("authorities", authorities)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .setIssuer("auth-service") // Add issuer
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String? {
        return try {
            val claims = parseClaimsFromToken(token)
            claims?.subject
        } catch (e: Exception) {
            logger.error("Error extracting username from JWT token: ${e.message}")
            null
        }
    }

    fun getEmailFromJwtToken(token: String): String? {
        return try {
            val claims = parseClaimsFromToken(token)
            claims?.get("email", String::class.java)
        } catch (e: Exception) {
            logger.error("Error extracting email from JWT token: ${e.message}")
            null
        }
    }

    fun getAuthoritiesFromJwtToken(token: String): List<String>? {
        return try {
            val claims = parseClaimsFromToken(token)
            @Suppress("UNCHECKED_CAST")
            claims?.get("authorities", List::class.java) as? List<String>
        } catch (e: Exception) {
            logger.error("Error extracting authorities from JWT token: ${e.message}")
            null
        }
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            val claims = parseClaimsFromToken(authToken)
            if (claims == null) {
                logger.error("Failed to parse JWT claims")
                return false
            }

            // Additional validation can be added here
            val now = Date()
            if (claims.expiration.before(now)) {
                logger.error("JWT token is expired")
                return false
            }

            logger.debug("JWT token validation successful")
            true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: ${e.message}")
            false
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token format: ${e.message}")
            false
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: ${e.message}")
            false
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: ${e.message}")
            false
        } catch (e: Exception) {
            logger.error("Unexpected error during JWT validation: ${e.message}")
            false
        }
    }

    private fun parseClaimsFromToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: JwtException) {
            logger.error("JWT parsing error: ${e.javaClass.simpleName} - ${e.message}")
            null
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid JWT token format: ${e.message}")
            null
        }
    }

    fun getExpirationDateFromToken(token: String): Date? {
        return try {
            val claims = parseClaimsFromToken(token)
            claims?.expiration
        } catch (e: Exception) {
            logger.error("Error extracting expiration date from JWT token: ${e.message}")
            null
        }
    }

    fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration?.before(Date()) ?: true
    }
}