package com.example.demo.service.impl;

import com.google.cloud.dialogflow.v2.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DialogflowServiceImplTest {

    @Test
    @DisplayName("detectIntent - 정상 응답")
    void detectIntent_success() {
        // given
        SessionsClient mockSessionsClient = Mockito.mock(SessionsClient.class);
        String projectId = "test-project";
        String sessionId = "test-session";
        String text = "안녕하세요";
        String languageCode = "ko";

        QueryResult queryResult = QueryResult.newBuilder().setFulfillmentText("테스트 응답").build();
        DetectIntentResponse response = DetectIntentResponse.newBuilder().setQueryResult(queryResult).build();
        Mockito.when(mockSessionsClient.detectIntent(Mockito.any(DetectIntentRequest.class))).thenReturn(response);

        DialogflowServiceImpl service = new DialogflowServiceImpl(mockSessionsClient, projectId);

        // when
        DetectIntentResponse result = service.detectIntent(sessionId, text, languageCode);

        // then
        assertNotNull(result);
        assertEquals("테스트 응답", result.getQueryResult().getFulfillmentText());
    }

    @Test
    @DisplayName("detectIntent - 예외 발생 시 RuntimeException 반환")
    void detectIntent_exception() {
        // given
        SessionsClient mockSessionsClient = Mockito.mock(SessionsClient.class);
        String projectId = "test-project";
        String sessionId = "test-session";
        String text = "안녕하세요";
        String languageCode = "ko";

        Mockito.when(mockSessionsClient.detectIntent(Mockito.any(DetectIntentRequest.class)))
                .thenThrow(new RuntimeException("API 오류"));

        DialogflowServiceImpl service = new DialogflowServiceImpl(mockSessionsClient, projectId);

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                service.detectIntent(sessionId, text, languageCode)
        );
        assertTrue(ex.getMessage().contains("Dialogflow API 호출 중 오류 발생"));
    }
}