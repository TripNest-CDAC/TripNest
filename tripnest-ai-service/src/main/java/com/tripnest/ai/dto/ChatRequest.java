package com.tripnest.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "Please enter a question")
        @Size(max = 300, message = "Question can contain at most 300 characters")
        String message
) { }
