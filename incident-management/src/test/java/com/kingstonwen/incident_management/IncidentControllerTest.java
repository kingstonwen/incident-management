package com.kingstonwen.incident_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Incident testIncidentOne;
    private Incident testIncidentTwo;

    @BeforeEach
    void setUp() {
        testIncidentOne = new Incident();
        testIncidentOne.setId(1L);
        testIncidentOne.setTitle("Test Incident One");
        testIncidentOne.setDescription("Test Description One");
        testIncidentOne.setPriority(IncidentPriority.ONE);
        testIncidentOne.setStatus(IncidentStatus.OPEN);

        testIncidentTwo = new Incident();
        testIncidentTwo.setId(2L);
        testIncidentTwo.setTitle("Test Incident Two");
        testIncidentTwo.setDescription("Test Description Tow");
        testIncidentTwo.setPriority(IncidentPriority.TWO);
        testIncidentTwo.setStatus(IncidentStatus.IN_PROGRESS);
    }

    @Test
    void createIncident_success() throws Exception {
        when(incidentService.createOrUpdateIncident(anyString(), anyString(), any(), any()))
                .thenReturn(testIncidentOne);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testIncidentOne)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Incident One"))
                .andExpect(jsonPath("$.description").value("Test Description One"));
    }

    @Test
    void createIncident_conflict() throws Exception {
        when(incidentService.createOrUpdateIncident(anyString(), anyString(), any(), any()))
                .thenThrow(new IllegalArgumentException("Incident already exists"));

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testIncidentOne)))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllIncidents_paginated() throws Exception {
        List<Incident> incidents = Arrays.asList(testIncidentOne, testIncidentTwo);
        Page<Incident> incidentPage = new PageImpl<>(incidents, PageRequest.of(0, 10), 1);

        when(incidentService.getPaginatedIncidents(eq(0), eq(10)))
                .thenReturn(incidentPage);

        mockMvc.perform(get("/incidents/paginated?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Incident One"))
                .andExpect(jsonPath("$.content[1].title").value("Test Incident Two"));

    }

    @Test
    void getAllIncidents_nonPaginated() throws Exception {
        List<Incident> incidents = Arrays.asList(testIncidentOne, testIncidentTwo);

        when(incidentService.getAllIncidents()).thenReturn(incidents);

        mockMvc.perform(get("/incidents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Incident One"))
                .andExpect(jsonPath("$[1].title").value("Test Incident Two"));
    }

    @Test
    void getIncidentById_success() throws Exception {
        when(incidentService.getIncidentById(1L)).thenReturn(testIncidentOne);

        mockMvc.perform(get("/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident One"))
                .andExpect(jsonPath("$.description").value("Test Description One"));
    }

    @Test
    void getIncidentById_notFound() throws Exception {
        when(incidentService.getIncidentById(1L)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateIncident_success() throws Exception {
        when(incidentService.updateIncident(eq(1L), anyString(), anyString(), any(), any()))
                .thenReturn(testIncidentOne);

        mockMvc.perform(put("/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testIncidentOne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident One"));
    }

    @Test
    void updateIncident_notFound() throws Exception {
        when(incidentService.updateIncident(eq(1L), anyString(), anyString(), any(), any()))
                .thenThrow(new NoSuchElementException());

        mockMvc.perform(put("/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testIncidentOne)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteIncident_success() throws Exception {
        mockMvc.perform(delete("/incidents/1"))
                .andExpect(status().isNoContent());

        verify(incidentService, times(1)).deleteIncident(1L);
    }
}
