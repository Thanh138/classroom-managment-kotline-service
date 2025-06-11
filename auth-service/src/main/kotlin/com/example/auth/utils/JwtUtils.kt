package com.example.auth.utils

import com.example.auth.service.imp.UserDetailsImpl
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey
import io.jsonwebtoken.SignatureAlgorithm

@Component
class JwtUtils(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
    @Value("\${jwt.expiration}")
    private val jwtExpirationMs: Long
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    
    fun generateJwtToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = DateUtils.addMilliseconds(now, jwtExpirationMs.toInt())
        val email = if (userDetails is UserDetailsImpl) {
            userDetails.email
        } else {
            ""
        }
        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (e: Exception) {
            println("Error parsing JWT token: ${e.message}")
            null
        }
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken)
                .body

            // Check token expiration
            val now = Date()
            if (claims.expiration.before(now)) {
                println("JWT token is expired")
                return false
            }
            true
        } catch (e: JwtException) {
            println("JWT validation error: ${e.javaClass.simpleName} - ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            println("Invalid JWT token format: ${e.message}")
            false
        } catch (e: Exception) {
            println("Unexpected error during JWT validation: ${e.message}")
            false
        }
    }
}