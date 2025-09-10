package org.example.app.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * PUBLIC_INTERFACE
 * test_ConversationItemTest validates equality and field storage for ConversationItem and ConversationRole.
 */
class test_ConversationItemTest {

    @Test
    fun equality_and_fields_work() {
        val a = ConversationItem(ConversationRole.USER, "hello")
        val b = ConversationItem(ConversationRole.USER, "hello")
        val c = ConversationItem(ConversationRole.ASSISTANT, "hello")

        assertEquals(a, b)
        assertNotEquals(a, c)
        assertEquals(ConversationRole.USER, a.role)
        assertEquals("hello", a.message)
    }
}
