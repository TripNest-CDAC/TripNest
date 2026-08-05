package com.tripnest.ai.controller;

import com.tripnest.ai.dto.ChatRequest;
import com.tripnest.ai.dto.ChatResponse;
import com.tripnest.ai.service.TripNestChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final TripNestChatService chatService;

    public ChatController(TripNestChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(chatService.answer(request.message()));
    }
}
