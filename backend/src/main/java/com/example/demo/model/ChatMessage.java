
package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;
}
