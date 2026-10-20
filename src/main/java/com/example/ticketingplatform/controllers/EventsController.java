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
        return eventsService.createEvent(eventRequestBody);
    }

}
