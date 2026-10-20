package com.example.ticketingplatform.services;

import com.example.ticketingplatform.exceptions.InvalidEventException;
import com.example.ticketingplatform.models.Event;
import com.example.ticketingplatform.models.EventRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventsServiceTest {
    private EventsService eventsService;
    private final LocalDateTime start = LocalDateTime.of(2026, 11, 10, 18, 0);
    private final LocalDateTime end = start.plusHours(2);

    @BeforeEach
    void setUp() {
        eventsService = new EventsService();
    }

    @Test
    void getAllEventsIsEmptyInitially() {
        assertTrue(eventsService.getEvents().isEmpty());
    }

    @Test
    void createEventStoresAndReturnsEvent() throws InvalidEventException {
        Event created = eventsService.createEvent(validRequest());

        assertEquals("Concert", created.getName());
        assertEquals("Live music", created.getDescription());
        assertEquals("Main Hall", created.getVenue());
        assertEquals(start, created.getDateTime());
        assertEquals(end, created.getEndTime());
        assertEquals(List.of(created), eventsService.getEvents());
        assertNotNull(created.getId());
    }

    @Test
    void createEventRejectsEmptyName() {
        assertInvalid(request(" ", "Live music", "Main Hall", start, end),
                "Event name must not be empty.");
    }

    @Test
    void createEventRejectsEmptyDescription() {
        assertInvalid(request("Concert", "  ", "Main Hall", start, end),
                "Event description must not be empty.");
    }

    @Test
    void createEventRejectsEmptyVenue() {
        assertInvalid(request("Concert", "Live music", "\t", start, end),
                "Event venue must not be empty.");
    }

    @Test
    void createEventRejectsNullStartTime() {
        assertInvalid(request("Concert", "Live music", "Main Hall", null, end),
                "Event date and time must not be null.");
    }

    @Test
    void createEventRejectsNullEndTime() {
        assertInvalid(request("Concert", "Live music", "Main Hall", start, null),
                "Event end time must not be null.");
    }

    @Test
    void createEventRejectsEndTimeBeforeStart() {
        assertInvalid(request("Concert", "Live music", "Main Hall", start, start.minusMinutes(1)),
                "Event end time must be after the start time.");
    }

    @Test
    void createEventRejectsEndTimeEqualToStart() {
        assertInvalid(request("Concert", "Live music", "Main Hall", start, start),
                "Event end time must be after the start time.");
    }

    private EventRequestBody validRequest() {
        return request("Concert", "Live music", "Main Hall", start, end);
    }

    private EventRequestBody request(
            String name,
            String description,
            String venue,
            LocalDateTime dateTime,
            LocalDateTime endTime
    ) {
        return new EventRequestBody(name, description, venue, dateTime, endTime);
    }

    private void assertInvalid(EventRequestBody request, String expectedMessage) {
        InvalidEventException exception = assertThrows(
                InvalidEventException.class,
                () -> eventsService.createEvent(request)
        );

        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(eventsService.getEvents().isEmpty());
    }
}
