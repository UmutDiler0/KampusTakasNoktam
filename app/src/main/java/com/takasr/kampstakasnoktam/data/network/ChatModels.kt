package com.takasr.kampstakasnoktam.data.network

import com.google.gson.annotations.SerializedName

data class ChatConversation(
    val id: Int,
    @SerializedName("user1_id") val user1Id: String,
    @SerializedName("user2_id") val user2Id: String,
    @SerializedName("participant_name") val participantName: String,
    @SerializedName("participant_image_url") val participantImageUrl: String?,
    @SerializedName("last_message") val lastMessage: String?,
    @SerializedName("last_message_timestamp") val lastMessageTimestamp: String?,
    @SerializedName("unread_count") val unreadCount: Int = 0,
    @SerializedName("is_online") val isOnline: Boolean = false
)

data class ChatMessage(
    val id: Int,
    @SerializedName("conversation_id") val conversationId: Int,
    @SerializedName("sender_id") val senderId: String,
    val text: String,
    val timestamp: String,
    val status: String // SENT, DELIVERED, READ
)

data class SendMessageRequest(
    @SerializedName("target_user_id") val targetUserId: String,
    @SerializedName("text") val text: String,
    @SerializedName("conversation_id") val conversationId: Int
)
