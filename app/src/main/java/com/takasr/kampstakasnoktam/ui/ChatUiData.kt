package com.takasr.kampstakasnoktam.ui

// ─── Domain Models ────────────────────────────────────────────────────────────

data class ChatConversation(
    val id: Int,
    val participantName: String,
    val participantInitials: String,
    val participantImageUrl: String? = null,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
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
            isOnline = true
        ),
        ChatConversation(
            id = 2,
            participantName = "Elif Demir",
            participantInitials = "ED",
            lastMessage = "Tamam, yarın 10:00'da görüşelim.",
            timestamp = "12:05",
            unreadCount = 0,
            isOnline = false
        ),
        ChatConversation(
            id = 3,
            participantName = "Can Arslan",
            participantInitials = "CA",
            participantImageUrl = "https://picsum.photos/seed/user-3/200/200",
            lastMessage = "Fiyat konusunda anlaşırız, merak etme.",
            timestamp = "Dün",
            unreadCount = 1,
            isOnline = true
        ),
        ChatConversation(
            id = 4,
            participantName = "Zeynep Uğur",
            participantInitials = "ZU",
            lastMessage = "Ürünü aldım, teşekkürler 🙏",
            timestamp = "Dün",
            unreadCount = 0,
            isOnline = false
        ),
        ChatConversation(
            id = 5,
            participantName = "Burak Şahin",
            participantInitials = "BŞ",
            participantImageUrl = "https://picsum.photos/seed/user-5/200/200",
            lastMessage = "İlan hâlâ aktif mi?",
            timestamp = "Pzt",
            unreadCount = 0,
            isOnline = false
        )
    )

    val emptyConversations: List<ChatConversation> = emptyList()
}
