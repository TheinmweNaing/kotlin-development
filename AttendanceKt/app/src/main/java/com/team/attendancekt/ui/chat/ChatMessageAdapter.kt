package com.team.attendancekt.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.attendancekt.AttendanceApplication
import com.team.attendancekt.BR
import com.team.attendancekt.R
import com.team.attendancekt.model.entity.ChatMessage
import com.team.attendancekt.model.entity.MessageType

class ChatMessageAdapter : ListAdapter<ChatMessage, ChatMessageAdapter.ChatMessageViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)
        return when (msg.messageType) {
            MessageType.JOIN, MessageType.LEAVE -> R.layout.layout_chat_info
            MessageType.CHAT -> {
                if (msg.sender == AttendanceApplication.currentUser) {
                    R.layout.layout_chat_send
                } else {
                    R.layout.layout_chat_receive
                }
            }
        }
    }

    inner class ChatMessageViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: ChatMessage) {
            binding.setVariable(BR.msg, msg)
        }
    }
}