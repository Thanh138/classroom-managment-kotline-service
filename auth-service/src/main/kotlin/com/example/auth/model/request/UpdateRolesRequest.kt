package com.example.auth.model.request

import com.example.auth.model.RoleAction

data class UpdateRolesRequest(
    val roleName: String,
    val action: RoleAction // ADD or REMOVE
)
