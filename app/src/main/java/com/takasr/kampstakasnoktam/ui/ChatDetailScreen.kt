package com.takasr.kampstakasnoktam.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    conversationId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val conversation = remember(conversationId) {
        ChatMockData.conversations.find { it.id == conversationId }
            ?: ChatMockData.conversations.first()
    }

    val messages = remember { mutableStateListOf<ChatMessage>().also { list ->
        list.addAll(ChatMockData.initialMessages(conversationId))
    }}

    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var nextId by remember { mutableIntStateOf(messages.size + 1) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Auto-scroll to bottom when messages change or typing indicator appears
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size + if (isTyping) 1 else 0)
        }
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isBlank()) return
        focusManager.clearFocus()
        inputText = ""

        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val messageId = nextId++
        messages.add(ChatMessage(id = messageId, text = text, isMine = true, timestamp = now, status = MessageStatus.SENT))

        // Simulate typing → reply after 3 s
        scope.launch {
            delay(600)
            val index1 = messages.indexOfFirst { it.id == messageId }
            if (index1 != -1) {
                messages[index1] = messages[index1].copy(status = MessageStatus.DELIVERED)
            }
            delay(800)
            val index2 = messages.indexOfFirst { it.id == messageId }
            if (index2 != -1) {
                messages[index2] = messages[index2].copy(status = MessageStatus.READ)
            }
            delay(400)
            isTyping = true
            delay(3000)
            isTyping = false
            val reply = ChatMockData.autoReplies.random()
            val replyTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            messages.add(ChatMessage(id = nextId++, text = reply, isMine = false, timestamp = replyTime))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                    ) {
                        // Mini avatar
                        Box(contentAlignment = Alignment.BottomEnd) {
                            if (conversation.participantImageUrl != null) {
                                AsyncImage(
                                    model = conversation.participantImageUrl,
                                    contentDescription = conversation.participantName,
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.chat_detail_avatar_size))
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.chat_detail_avatar_size))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = conversation.participantInitials,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            if (conversation.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(Color(0xFF25D366))
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = conversation.participantName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (isTyping) {
                                Text(
                                    text = stringResource(R.string.chat_typing),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            } else if (conversation.isOnline) {
                                Text(
                                    text = stringResource(R.string.chat_online),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.seller_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Voice Call",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = ::sendMessage,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs)),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = dimensionResource(R.dimen.spacing_md),
                    bottom = dimensionResource(R.dimen.spacing_md)
                )
            ) {
                items(messages, key = { it.id }) { msg ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 280)
                        ) + fadeIn(animationSpec = tween(durationMillis = 280))
                    ) {
                        MessageBubble(message = msg)
                    }
                }

                // Typing indicator bubble
                item(key = "typing") {
                    AnimatedVisibility(
                        visible = isTyping,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(200)
                        ) + fadeIn(tween(200)),
                        exit = fadeOut(tween(150))
                    ) {
                        TypingIndicatorBubble()
                    }
                }
            }
        }
    }
}

// ─── Message Bubble ───────────────────────────────────────────────────────────

@Composable
private fun MessageBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (message.isMine) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (message.isMine) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val timeColor = if (message.isMine) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.70f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
    }

    // WhatsApp shape tails (top-right for mine, top-left for others)
    val shape = if (message.isMine) {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 2.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 2.dp,
            topEnd = 12.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = shape,
            color = bubbleColor,
            shadowElevation = 0.5.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = timeColor
                    )
                    if (message.isMine) {
                        MessageStatusTick(status = message.status)
                    }
                }
            }
        }
    }
}

// ─── Message Status Ticks ───────────────────────────────────────────────────

@Composable
private fun MessageStatusTick(status: MessageStatus, modifier: Modifier = Modifier.size(16.dp)) {
    when (status) {
        MessageStatus.SENT -> SingleCheckmark(modifier)
        MessageStatus.DELIVERED -> DoubleCheckmark(isRead = false, modifier)
        MessageStatus.READ -> DoubleCheckmark(isRead = true, modifier)
    }
}

@Composable
private fun SingleCheckmark(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(4.dp.toPx(), 8.dp.toPx())
            lineTo(7.dp.toPx(), 11.dp.toPx())
            lineTo(12.dp.toPx(), 5.dp.toPx())
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
private fun DoubleCheckmark(isRead: Boolean, modifier: Modifier = Modifier) {
    val color = if (isRead) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
    Canvas(modifier = modifier) {
        val path1 = Path().apply {
            moveTo(2.dp.toPx(), 8.dp.toPx())
            lineTo(5.dp.toPx(), 11.dp.toPx())
            lineTo(10.dp.toPx(), 5.dp.toPx())
        }
        drawPath(
            path = path1,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        
        val path2 = Path().apply {
            moveTo(6.dp.toPx(), 8.dp.toPx())
            lineTo(9.dp.toPx(), 11.dp.toPx())
            lineTo(14.dp.toPx(), 5.dp.toPx())
        }
        drawPath(
            path = path2,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

// ─── Typing Indicator ─────────────────────────────────────────────────────────

@Composable
private fun TypingIndicatorBubble(modifier: Modifier = Modifier) {
    val bubbleColor = MaterialTheme.colorScheme.surface
    
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 2.dp,
                topEnd = 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            ),
            color = bubbleColor,
            shadowElevation = 0.5.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TypingDot(progress = animationProgress, index = 0)
                TypingDot(progress = animationProgress, index = 1)
                TypingDot(progress = animationProgress, index = 2)
            }
        }
    }
}

@Composable
private fun TypingDot(progress: Float, index: Int) {
    val phaseOffset = index * 2.0 * Math.PI / 3.0
    val angle = (progress * 2.0 * Math.PI) - phaseOffset
    val offsetY = (Math.sin(angle) * 4.dp.value).toFloat()
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .offset(y = offsetY.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
    )
}

// ─── Input Bar ────────────────────────────────────────────────────────────────

@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inputBgColor = MaterialTheme.colorScheme.surface
    val iconColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(25.dp),
            color = inputBgColor,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Mood,
                        contentDescription = "Emojis",
                        tint = iconColor
                    )
                }

                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.chat_type_hint),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSend() }),
                    maxLines = 4,
                    singleLine = false
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = iconColor
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        tint = iconColor
                    )
                }
            }
        }

        val hasText = value.trim().isNotEmpty()
        IconButton(
            onClick = { if (hasText) onSend() else {} },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = if (hasText) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                contentDescription = if (hasText) "Send" else "Record Voice",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
