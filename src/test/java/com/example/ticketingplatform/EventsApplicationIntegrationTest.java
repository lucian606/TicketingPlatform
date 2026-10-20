package com.example.ticketingplatform;

import com.example.ticketingplatform.services.EventsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventsApplicationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventsService eventsService;

    @Test
    void createEventPersistsItThroughTheApplication() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Integration test concert",
                                  "description": "Live music",
                                  "venue": "Main Hall",
                                  "dateTime": "2026-11-10T18:00:00",
                                  "endTime": "2026-11-10T20:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Integration test concert"))
                .andExpect(jsonPath("$.description").value("Live music"))
                .andExpect(jsonPath("$.venue").value("Main Hall"));

        assertTrue(eventsService.getAllEvents().stream()
                .anyMatch(event -> event.getName().equals("Integration test concert")));
    }

    @Test
    void createEventReturnsBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
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
}
