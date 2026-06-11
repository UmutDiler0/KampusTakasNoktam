package com.takasr.kampstakasnoktam.data.model

import com.takasr.kampstakasnoktam.data.network.ChatConversation
import com.takasr.kampstakasnoktam.data.network.ChatMessage

enum class MessageStatus { SENT, DELIVERED, READ }

data class Conversation(
    val id: Int,
    val user1Id: String,
    val user2Id: String,
    val participantName: String,
    val participantImageUrl: String? = null,
    val lastMessage: String? = null,
    val lastMessageTimestamp: String? = null,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
) {
    val participantInitials: String
        get() = participantName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
}

data class Message(
    val id: Int,
    val conversationId: Int,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val status: MessageStatus = MessageStatus.SENT
)

fun ChatConversation.toConversation(): Conversation = Conversation(
    id = id,
    user1Id = user1Id,
    user2Id = user2Id,
    participantName = participantName,
    participantImageUrl = participantImageUrl,
    lastMessage = lastMessage,
    lastMessageTimestamp = lastMessageTimestamp,
    unreadCount = unreadCount,
    isOnline = isOnline
)

fun ChatMessage.toMessage(): Message = Message(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    text = text,
    timestamp = timestamp,
    status = when (status) {
        "DELIVERED" -> MessageStatus.DELIVERED
        "READ" -> MessageStatus.READ
        else -> MessageStatus.SENT
    }
)
