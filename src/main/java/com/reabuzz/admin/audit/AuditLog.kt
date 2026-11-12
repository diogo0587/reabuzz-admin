package com.reabuzz.admin.audit

import com.reabuzz.admin.model.Account

enum class Outcome { SUCCESS, DENIED, FAILURE }

data class AuditEntry(
    val timestampMs: Long,
    val actorId: Long,
    val actorUsername: String,
    val action: String,
    val details: String?,
    val outcome: Outcome,
)

interface AuditLog {
    fun record(entry: AuditEntry)
    fun entries(): List<AuditEntry>
}

/**
 * Process-local audit log. Replace with a server-backed sink so entries survive
 * process death and cannot be dropped by the audited party.
 */
class InMemoryAuditLog(private val clock: () -> Long = System::currentTimeMillis) : AuditLog {

    private val entries = mutableListOf<AuditEntry>()

    override fun record(entry: AuditEntry) {
        entries += entry
    }

    override fun entries(): List<AuditEntry> = entries.toList()

    fun recordFor(actor: Account, action: String, details: String?, outcome: Outcome) {
        record(
            AuditEntry(
                timestampMs = clock(),
                actorId = actor.id,
                actorUsername = actor.username,
                action = action,
                details = details,
                outcome = outcome,
            )
        )
    }
}
