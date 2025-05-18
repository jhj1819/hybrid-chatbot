
package com.example.demo.repository;

import com.example.demo.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findTop10BySessionIdOrderByTimestampDesc(String sessionId);
}
