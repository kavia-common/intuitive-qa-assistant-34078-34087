package org.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * PUBLIC_INTERFACE
 * ConversationAdapter renders chat messages in a RecyclerView with different styling
 * for user, assistant, and system messages in a dark theme.
 */
class ConversationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<Message>()

    fun submit(items: List<Message>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].role) {
            Role.User -> 1
            Role.Assistant -> 2
            Role.System -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> UserVH(inflater.inflate(R.layout.item_message_user, parent, false))
            2 -> AssistantVH(inflater.inflate(R.layout.item_message_assistant, parent, false))
            else -> SystemVH(inflater.inflate(R.layout.item_message_system, parent, false))
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is UserVH -> holder.bind(item)
            is AssistantVH -> holder.bind(item)
            is SystemVH -> holder.bind(item)
        }
    }

    private class UserVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.messageText)
        fun bind(item: Message) {
            text.text = item.content
        }
    }

    private class AssistantVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.messageText)
        fun bind(item: Message) {
            text.text = item.content
        }
    }

    private class SystemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.messageText)
        fun bind(item: Message) {
            text.text = item.content
        }
    }
}
