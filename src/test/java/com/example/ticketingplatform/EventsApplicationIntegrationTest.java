package com.example.ticketingplatform;

import com.example.ticketingplatform.services.EventsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        assertTrue(eventsService.getEvents().stream()
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

    @Test
    void getEventsReturnsEventsCreatedThroughTheApplication() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Event listed by integration test",
                                  "description": "A test event",
                                  "venue": "Test Hall",
                                  "dateTime": "2026-11-10T18:00:00",
                                  "endTime": "2026-11-10T20:00:00"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..name", hasItem("Event listed by integration test")));
    }
}
