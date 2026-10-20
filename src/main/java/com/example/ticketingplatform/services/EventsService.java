package com.example.ticketingplatform.services;

import com.example.ticketingplatform.exceptions.InvalidEventException;
import com.example.ticketingplatform.models.Event;
import com.example.ticketingplatform.models.EventRequestBody;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventsService {
    private final List<Event> events = new ArrayList<>();

    public List<Event> getEvents() {
        return List.copyOf(events);
    }

    public Event createEvent(EventRequestBody eventRequestBody) throws InvalidEventException {
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

        if (eventRequestBody.endTime().isBefore(eventRequestBody.dateTime()) || eventRequestBody.endTime().isEqual(eventRequestBody.dateTime())) {
            throw new InvalidEventException("Event end time must be after the start time.");
        }

        Event event = new Event(
                eventRequestBody.name(),
                eventRequestBody.description(),
                eventRequestBody.venue(),
                eventRequestBody.dateTime(),
                eventRequestBody.endTime()
        );

        events.add(event);
        return event;
    }
}
