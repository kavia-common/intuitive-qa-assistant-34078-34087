package org.example.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.example.app.R

/**
 * PUBLIC_INTERFACE
 * ConversationAdapter renders ConversationItem instances in a ListView.
 * It supports two view types: USER and ASSISTANT, with separate item layouts.
 */
class ConversationAdapter(
    private val context: Context,
    private val items: List<ConversationItem>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount(): Int = 2

    override fun getItemViewType(position: Int): Int {
        return when (items[position].role) {
            ConversationRole.USER -> 0
            ConversationRole.ASSISTANT -> 1
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = items[position]
        val type = getItemViewType(position)

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = if (type == 0) {
                inflater.inflate(R.layout.item_message_user, parent, false)
            } else {
                inflater.inflate(R.layout.item_message_assistant, parent, false)
            }
            holder = ViewHolder(view.findViewById(R.id.tvMessage))
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.message.text = item.message
        return view
    }

    private data class ViewHolder(val message: TextView)
}
