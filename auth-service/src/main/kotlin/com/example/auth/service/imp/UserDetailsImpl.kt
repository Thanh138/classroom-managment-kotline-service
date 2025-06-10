package com.example.auth.service.imp

import com.example.auth.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: Long,
    private val username: String,
    val email: String,
    @field:JsonIgnore
    private val password: String,
    private val enabled: Boolean,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }.toSet()

            return UserDetailsImpl(
                user.id,
                user.username,
                user.email,
                user.password,
                user.isActive,
                authorities
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = enabled
}