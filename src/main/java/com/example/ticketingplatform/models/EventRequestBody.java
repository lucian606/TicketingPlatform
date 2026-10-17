package com.example.ticketingplatform.models;

import java.time.LocalDateTime;

public record EventRequestBody(
        String name,
        String description,
        String venue,
        LocalDateTime dateTime,
        LocalDateTime endTime
) {
}
