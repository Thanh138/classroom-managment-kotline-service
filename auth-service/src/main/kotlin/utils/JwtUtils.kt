package com.example.utils

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.commons.lang.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date
import javax.crypto.SecretKey

class JwtUtils(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
    @Value("\${jwt.expirationMs}")
    private val jwtExpirationMs: Long
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    fun generateJwtToken(authentication: UserDetails): String {
        val now = Date()
        val expiryDate = DateUtils.addMilliseconds(now, jwtExpirationMs.toInt())
        return Jwts.builder().setSubject(authentication.username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun getUserInfoFromJwtToken(token: String): String? {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            println("Error parsing JWT token: ${e.message}")
            null
        }
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken)
            true
        } catch (e: JwtException) {
            println("Invalid JWT token: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            println("Invalid JWT token format: ${e.message}")
            false
        }
    }
}