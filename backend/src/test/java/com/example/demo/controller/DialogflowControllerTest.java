package com.example.demo.controller;

import com.example.demo.service.DialogflowService;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DialogflowController.class)
class DialogflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DialogflowService dialogflowService;

    @Test
    @DisplayName("POST /api/dialogflow/detect-intent - 정상 응답")
    void detectIntent_success() throws Exception {
        // given
        QueryResult queryResult = QueryResult.newBuilder().setFulfillmentText("테스트 응답").build();
        DetectIntentResponse response = DetectIntentResponse.newBuilder().setQueryResult(queryResult).build();
        Mockito.when(dialogflowService.detectIntent(eq("test-session"), eq("안녕하세요"), eq("ko"))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/dialogflow/detect-intent")
                        .param("sessionId", "test-session")
                        .param("text", "안녕하세요")
                        .param("languageCode", "ko")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfillmentText").value("테스트 응답"));
    }
}