package com.example.ticketingplatform.services;

import com.example.ticketingplatform.models.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventsService {
    private final List<Event> events = new ArrayList<>();

    public List<Event> getAllEvents() {
        return events;
    }

    public Event createEvent(Event event) {
        events.add(event);
        return event;
    }
}
