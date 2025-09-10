package org.example.app

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * PUBLIC_INTERFACE
 * test_MessageUtilsExtraTest adds additional assertion on MessageUtils formatting.
 */
class test_MessageUtilsExtraTest {
    @Test
    fun message_hasExpectedSpaces() {
        // Align with implementation in MessageUtils: "Hello     World!" (5 spaces)
        assertEquals("Hello     World!", MessageUtils.message())
    }
}
