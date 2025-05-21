package com.example.demo.controller;

import com.example.demo.dto.MessageRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.service.ChatService;
import com.example.demo.model.ChatMessage;
import com.example.demo.service.OpenAiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import java.util.List;
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final OpenAiService openAiService;
    private final ChatService chatService;

    @PostMapping("/receive")
    public Mono<ResponseEntity<MessageResponse>> receiveMessage(@Valid @RequestBody MessageRequest request) {
        String sessionId = request.getUserId();
        chatService.saveMessage(sessionId, request.getUserId(), "user", request.getMessage());

        return openAiService.getChatCompletion(request.getMessage())
                .map(reply -> {
                    chatService.saveMessage(sessionId, request.getUserId(), "bot", reply);
                    MessageResponse response = MessageResponse.builder()
                            .userId(request.getUserId())
                            .response(reply)
                            .build();
                    return ResponseEntity.ok(response);  // ✅ 이 부분이 핵심
                });
    }

    @GetMapping("/send")
    public ResponseEntity<List<ChatMessage>> sendMessages(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // ✅ 400 Bad Request 반환
        }
        List<ChatMessage> messages = chatService.getRecentMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }
}
