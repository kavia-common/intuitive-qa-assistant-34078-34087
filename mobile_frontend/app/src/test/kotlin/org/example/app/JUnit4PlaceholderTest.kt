package org.example.app

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * PUBLIC_INTERFACE
 * JUnit4PlaceholderTest uses JUnit 4 so it is discovered by the default Android
 * unit test runner even if JUnit 5 platform is not configured for unit tests.
 */
class JUnit4PlaceholderTest {
    @Test
    fun runsUnderJunit4() {
        assertTrue(true)
    }
}
