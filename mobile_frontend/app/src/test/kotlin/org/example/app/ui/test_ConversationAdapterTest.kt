package org.example.app.ui

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

/**
 * PUBLIC_INTERFACE
 * test_ConversationAdapterTest validates ConversationAdapter view type mapping
 * and count using a mocked Context to avoid Android runtime dependencies.
 */
class test_ConversationAdapterTest {

    @Test
    fun getCountAndItemType_areCorrect() {
        val items = listOf(
            ConversationItem(ConversationRole.USER, "Hi"),
            ConversationItem(ConversationRole.ASSISTANT, "Hello, how can I help?")
        )
        val context = Mockito.mock(Context::class.java, Mockito.RETURNS_DEEP_STUBS)

        val adapter = ConversationAdapter(context, items)

        assertEquals(2, adapter.count)
        assertEquals(0, adapter.getItemViewType(0))
        assertEquals(1, adapter.getItemViewType(1))
        assertEquals(2, adapter.viewTypeCount)

        // Verify getItem returns expected item without dereferencing Android UI
        val first = adapter.getItem(0) as ConversationItem
        val second = adapter.getItem(1) as ConversationItem
        assertEquals("Hi", first.message)
        assertEquals("Hello, how can I help?", second.message)
    }
}
