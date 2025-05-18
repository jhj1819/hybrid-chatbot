
package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(String sessionId, String userId, String sender, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .sessionId(sessionId)
                .userId(userId)
                .sender(sender)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getRecentMessages(String sessionId) {
        return chatMessageRepository.findTop10BySessionIdOrderByTimestampDesc(sessionId);
    }
}
