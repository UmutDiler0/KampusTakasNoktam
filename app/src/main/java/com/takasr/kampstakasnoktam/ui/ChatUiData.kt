package com.takasr.kampstakasnoktam.ui

// ─── Domain Models ────────────────────────────────────────────────────────────

enum class MessageStatus {
    SENT, DELIVERED, READ
}

data class ChatConversation(
    val id: Int,
    val participantName: String,
    val participantInitials: String,
    val participantImageUrl: String? = null,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val lastMessageIsMine: Boolean = false,
    val lastMessageStatus: MessageStatus = MessageStatus.READ
)

data class ChatMessage(
    val id: Int,
    val text: String,
    val isMine: Boolean,
    val timestamp: String,
    val status: MessageStatus = MessageStatus.READ
)

// ─── Mock Data ────────────────────────────────────────────────────────────────

object ChatMockData {

    val conversations = listOf(
        ChatConversation(
            id = 1,
            participantName = "Mehmet Türk",
            participantInitials = "MT",
            participantImageUrl = "https://picsum.photos/seed/user-1/200/200",
            lastMessage = "MacBook hâlâ satılık mı?",
            timestamp = "14:32",
            unreadCount = 3,
            isOnline = true,
            lastMessageIsMine = false
        ),
        ChatConversation(
            id = 2,
            participantName = "Elif Demir",
            participantInitials = "ED",
            lastMessage = "Tamam, yarın 10:00'da görüşelim.",
            timestamp = "12:05",
            unreadCount = 0,
            isOnline = false,
            lastMessageIsMine = true,
            lastMessageStatus = MessageStatus.READ
        ),
        ChatConversation(
            id = 3,
            participantName = "Can Arslan",
            participantInitials = "CA",
            participantImageUrl = "https://picsum.photos/seed/user-3/200/200",
            lastMessage = "Fiyat konusunda anlaşırız, merak etme.",
            timestamp = "Dün",
            unreadCount = 1,
            isOnline = true,
            lastMessageIsMine = false
        ),
        ChatConversation(
            id = 4,
            participantName = "Zeynep Uğur",
            participantInitials = "ZU",
            lastMessage = "Ürünü aldım, teşekkürler 🙏",
            timestamp = "Dün",
            unreadCount = 0,
            isOnline = false,
            lastMessageIsMine = true,
            lastMessageStatus = MessageStatus.READ
        ),
        ChatConversation(
            id = 5,
            participantName = "Burak Şahin",
            participantInitials = "BŞ",
            participantImageUrl = "https://picsum.photos/seed/user-5/200/200",
            lastMessage = "İlan hâlâ aktif mi?",
            timestamp = "Pzt",
            unreadCount = 0,
            isOnline = false,
            lastMessageIsMine = false
        )
    )

    val emptyConversations: List<ChatConversation> = emptyList()

    /** Initial message history shown when a thread is opened. */
    fun initialMessages(conversationId: Int): List<ChatMessage> = when (conversationId) {
        1 -> listOf(
            ChatMessage(1, "Merhaba! MacBook satılık mı?", false, "14:28"),
            ChatMessage(2, "Evet, hâlâ satılık.", true, "14:29", MessageStatus.READ),
            ChatMessage(3, "Fiyatta indirim yapabilir misiniz?", false, "14:30"),
            ChatMessage(4, "Küçük bir indirim yapabilirim, makul teklif bekliyorum.", true, "14:31", MessageStatus.READ),
            ChatMessage(5, "MacBook hâlâ satılık mı?", false, "14:32")
        )
        2 -> listOf(
            ChatMessage(1, "Merhaba, masayı hâlâ satıyor musunuz?", false, "11:50"),
            ChatMessage(2, "Evet! Ne zaman bakabilirsiniz?", true, "11:52", MessageStatus.READ),
            ChatMessage(3, "Yarın sabah müsaitim.", false, "11:58"),
            ChatMessage(4, "Tamam, yarın 10:00'da görüşelim.", false, "12:05")
        )
        else -> listOf(
            ChatMessage(1, "Merhaba, ilanınızı gördüm. İlgileniyorum.", false, "09:00"),
            ChatMessage(2, "Harika! Detayları konuşabiliriz.", true, "09:05", MessageStatus.READ)
        )
    }

    /** Pool of auto-replies used for the simulated response. */
    val autoReplies = listOf(
        "Anlıyorum, biraz düşüneyim.",
        "Peki, fiyatı uygun bulursam alırım.",
        "Teşekkürler, haberdar ederim!",
        "Tamam, buluşma yeri için anlaşalım.",
        "Güzel, en kısa sürede cevaplarım.",
        "Olur, yarın tekrar konuşalım.",
        "Anlaştık! 👍",
        "Harika, bekliyorum."
    )
}
