
package com.example.demo.controller;

import com.example.demo.dto.MessageRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.service.ChatService;
import com.example.demo.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final OpenAiService openAiService;
    private final ChatService chatService;

    @PostMapping("/receive")
    public Mono<MessageResponse> receiveMessage(@RequestBody MessageRequest request) {
        String sessionId = request.getUserId();
        chatService.saveMessage(sessionId, request.getUserId(), "user", request.getMessage());

        return openAiService.getChatCompletion(request.getMessage())
                .map(reply -> {
                    chatService.saveMessage(sessionId, request.getUserId(), "bot", reply);
                    return MessageResponse.builder()
                            .userId(request.getUserId())
                            .response(reply)
                            .build();
                });
    }
}
