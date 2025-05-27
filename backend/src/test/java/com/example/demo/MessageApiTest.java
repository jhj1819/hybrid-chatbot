package com.example.demo;

import com.example.demo.controller.MessageController;
import com.example.demo.dto.MessageRequest;
import com.example.demo.model.ChatMessage;
import com.example.demo.service.ChatService;
import com.example.demo.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebFluxTest(MessageController.class)
public class MessageApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OpenAiService openAiService;

    @MockBean
    private ChatService chatService;

    @BeforeEach
    void setup() {
        when(openAiService.getChatCompletion(anyString()))
                .thenReturn(Mono.just("테스트 응답입니다."));

        List<ChatMessage> mockMessages = Arrays.asList(
                ChatMessage.builder().userId("testUser123").sender("user").message("안녕! 챗봇이야?").build(),
                ChatMessage.builder().userId("testUser123").sender("bot").message("테스트 응답입니다.").build()
        );

        when(chatService.getRecentMessagesByUserId(anyString())).thenReturn(mockMessages);
        doNothing().when(chatService).saveMessage(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testReceiveMessageAndStoreInMongoDB() {
        MessageRequest request = MessageRequest.builder()
                .userId("testUser123")
                .message("안녕! 챗봇이야?")
                .build();

        webTestClient.post()
                .uri("/api/messages/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("testUser123")
                .jsonPath("$.response").isEqualTo("테스트 응답입니다.");
    }

    @Test
    void testSendMessageHistory() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/messages/send")
                        .queryParam("userId", "testUser123")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ChatMessage.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<ChatMessage> messages = response.getResponseBody();
                    assert messages != null;
                    assert messages.get(0).getUserId().equals("testUser123");
                    assert messages.get(1).getSender().equals("bot");
                });
    }

    @Test
    void testReceiveMessageWithInvalidRequest() {
        MessageRequest invalidRequest = MessageRequest.builder()
                .userId("")
                .message("")
                .build();

        webTestClient.post()
                .uri("/api/messages/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testSendMessageHistoryWithInvalidUserId() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/messages/send")
                        .queryParam("userId", "")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
