package com.example.demo.controller;

import com.example.demo.service.DialogflowService;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dialogflow")
@RequiredArgsConstructor

public class DialogflowController {

    private final DialogflowService dialogflowService;

    @PostMapping("/detect-intent")
    public ResponseEntity<?> detectIntent(
            @RequestParam String sessionId,
            @RequestParam String text,
            @RequestParam(defaultValue = "ko") String languageCode) {

        DetectIntentResponse response = dialogflowService.detectIntent(sessionId, text, languageCode);

        // fulfillmentText 등 필요한 정보만 추출
        String fulfillmentText = response.getQueryResult().getFulfillmentText();

        // 필요하다면 추가 정보도 추출 가능
        // String intentName = response.getQueryResult().getIntent().getDisplayName();

        // 결과를 Map에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("fulfillmentText", fulfillmentText);
        // result.put("intentName", intentName);

        return ResponseEntity.ok(result);
    }
}