package com.longbark.maintenance.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Long
)
