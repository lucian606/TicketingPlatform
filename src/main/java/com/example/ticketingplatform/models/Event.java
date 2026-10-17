package com.example.ticketingplatform.models;

import java.util.UUID;
import java.time.LocalDateTime;

public class Event {
    private UUID id;
    private String name;
    private String description;
    private String venue;
    private LocalDateTime dateTime;
    private LocalDateTime endTime;

    public Event(
            String name,
            String description,
            String venue,
            LocalDateTime dateTime,
            LocalDateTime endTime
    ) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.dateTime = dateTime;
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}