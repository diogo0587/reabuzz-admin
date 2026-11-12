package com.reabuzz.admin.audit

import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemoryAuditLogTest {

    private val actor = Account(7, "owner", setOf(Role.USER, Role.ADMIN, Role.VIP))

    @Test
    fun `recordFor captures actor action and outcome`() {
        val log = InMemoryAuditLog(clock = { 1_000L })
        log.recordFor(actor, "ASSIGN_ROLE", "2 -> VIP", Outcome.SUCCESS)

        val entry = log.entries().single()
        assertEquals(1_000L, entry.timestampMs)
        assertEquals(7L, entry.actorId)
        assertEquals("owner", entry.actorUsername)
        assertEquals("ASSIGN_ROLE", entry.action)
        assertEquals("2 -> VIP", entry.details)
        assertEquals(Outcome.SUCCESS, entry.outcome)
    }

    @Test
    fun `entries reflects every recorded entry in order`() {
        val log = InMemoryAuditLog()
        log.recordFor(actor, "FIRST", null, Outcome.SUCCESS)
        log.recordFor(actor, "SECOND", null, Outcome.DENIED)

        assertEquals(listOf("FIRST", "SECOND"), log.entries().map { it.action })
    }

    @Test
    fun `denied attempts are still recorded`() {
        val log = InMemoryAuditLog()
        log.recordFor(actor, "ASSIGN_ROLE", null, Outcome.DENIED)
        assertTrue(log.entries().any { it.outcome == Outcome.DENIED })
    }
}
