package com.example.ticketingplatform.controllers;

import com.example.ticketingplatform.exceptions.InvalidEventException;
import com.example.ticketingplatform.models.Event;
import com.example.ticketingplatform.models.EventRequestBody;
import com.example.ticketingplatform.services.EventsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping
    public String hello() {
        return "Hello from EventsController!";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(
            @RequestBody EventRequestBody eventRequestBody
    ) throws InvalidEventException {
        if (eventRequestBody.name() == null || eventRequestBody.name().trim().isEmpty()) {
            throw new InvalidEventException("Event name must not be empty.");
        }

        if (eventRequestBody.description() == null || eventRequestBody.description().trim().isEmpty()) {
            throw new InvalidEventException("Event description must not be empty.");
        }

        if (eventRequestBody.venue() == null || eventRequestBody.venue().trim().isEmpty()) {
            throw new InvalidEventException("Event venue must not be empty.");
        }

        if (eventRequestBody.dateTime() == null) {
            throw new InvalidEventException("Event date and time must not be null.");
        }

        if (eventRequestBody.endTime() == null) {
            throw new InvalidEventException("Event end time must not be null.");
        }

        if (eventRequestBody.endTime().isBefore(eventRequestBody.dateTime())) {
            throw new InvalidEventException("Event end time must be after the start time.");
        }

        Event event = new Event(
                eventRequestBody.name(),
                eventRequestBody.description(),
                eventRequestBody.venue(),
                eventRequestBody.dateTime(),
                eventRequestBody.endTime()
        );

        return eventsService.createEvent(event);
    }

}
