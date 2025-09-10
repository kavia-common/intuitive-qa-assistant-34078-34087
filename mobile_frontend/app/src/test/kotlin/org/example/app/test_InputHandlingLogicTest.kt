package org.example.app

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * PUBLIC_INTERFACE
 * test_InputHandlingLogicTest verifies input handling expectations analogous to MainActivity:
 * - Send button should be disabled for blank text.
 * - Enabled for non-blank.
 *
 * This is a pure unit abstraction test asserting the rule used in TextWatcher.
 */
class test_InputHandlingLogicTest {

    private fun canSend(text: CharSequence?): Boolean = !text.isNullOrBlank()

    @Test
    fun blankInput_disablesSend() {
        assertFalse(canSend(null))
        assertFalse(canSend(""))
        assertFalse(canSend("   "))
        assertFalse(canSend("\n"))
    }

    @Test
    fun nonBlank_enablesSend() {
        assertTrue(canSend("a"))
        assertTrue(canSend(" hi "))
    }
}
