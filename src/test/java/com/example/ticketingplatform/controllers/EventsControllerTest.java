package com.example.ticketingplatform.controllers;

import com.example.ticketingplatform.exceptions.EventsExceptionHandler;
import com.example.ticketingplatform.exceptions.InvalidEventException;
import com.example.ticketingplatform.models.Event;
import com.example.ticketingplatform.models.EventRequestBody;
import com.example.ticketingplatform.services.EventsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class EventsControllerTest {
    private EventsService eventsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        eventsService = mock(EventsService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EventsController(eventsService))
                .setControllerAdvice(new EventsExceptionHandler())
                .build();
    }

    @Test
    void createEventReturnsCreatedEvent() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 11, 10, 18, 0);
        LocalDateTime end = start.plusHours(2);
        Event event = new Event("Concert", "Live music", "Main Hall", start, end);
        when(eventsService.createEvent(any(EventRequestBody.class))).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Concert",
                                  "description": "Live music",
                                  "venue": "Main Hall",
                                  "dateTime": "2026-11-10T18:00:00",
                                  "endTime": "2026-11-10T20:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(event.getId().toString()))
                .andExpect(jsonPath("$.name").value("Concert"))
                .andExpect(jsonPath("$.description").value("Live music"))
                .andExpect(jsonPath("$.venue").value("Main Hall"));

        verify(eventsService).createEvent(new EventRequestBody(
                "Concert", "Live music", "Main Hall", start, end
        ));
    }

    @Test
    void getEventsReturnsEventsFromService() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 11, 10, 18, 0);
        Event event = new Event("Concert", "Live music", "Main Hall", start, start.plusHours(2));
        when(eventsService.getEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(event.getId().toString()))
                .andExpect(jsonPath("$[0].name").value("Concert"))
                .andExpect(jsonPath("$[0].description").value("Live music"))
                .andExpect(jsonPath("$[0].venue").value("Main Hall"));

        verify(eventsService).getEvents();
    }

    @Test
    void createEventReturnsBadRequestForInvalidEvent() throws Exception {
        when(eventsService.createEvent(any(EventRequestBody.class)))
                .thenThrow(new InvalidEventException("Event name must not be empty."));

        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": " ",
                                  "description": "Live music",
                                  "venue": "Main Hall",
                                  "dateTime": "2026-11-10T18:00:00",
                                  "endTime": "2026-11-10T20:00:00"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Event name must not be empty."));
    }

    @Test
    void createEventReturnsBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body"));
    }
}
