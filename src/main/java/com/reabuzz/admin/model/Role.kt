package com.reabuzz.admin.model

/**
 * Roles are additive. VIP is independent of the administrative roles, so an
 * account can be both ADMIN and VIP.
 */
enum class Role {
    USER,
    MODERATOR,
    VIP,
    ADMIN,
}

data class Account(
    val id: Long,
    val username: String,
    val roles: Set<Role> = setOf(Role.USER),
) {
    val isAdmin: Boolean get() = Role.ADMIN in roles
    val isVip: Boolean get() = Role.VIP in roles
    val isModerator: Boolean get() = Role.MODERATOR in roles
}
