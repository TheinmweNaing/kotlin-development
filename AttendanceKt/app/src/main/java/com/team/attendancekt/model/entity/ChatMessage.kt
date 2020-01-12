package com.team.attendancekt.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatMessage(
    @JsonProperty("message_type")
    var messageType: MessageType = MessageType.CHAT,
    var content: String? = "",
    var sender: String? = "",
    var to: String? = ""

)

enum class MessageType {
    CHAT, JOIN, LEAVE
}