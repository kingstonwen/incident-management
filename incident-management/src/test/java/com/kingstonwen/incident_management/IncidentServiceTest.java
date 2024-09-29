package com.kingstonwen.incident_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IncidentServiceTest {

    // Constants for test data
    private static final String TEST_TITLE = "Title";
    private static final String TEST_DESCRIPTION = "Description";
    private static final Long TEST_INCIDENT_ID = 1L;
    private static final IncidentPriority TEST_PRIORITY = IncidentPriority.ONE;
    private static final IncidentStatus TEST_STATUS = IncidentStatus.OPEN;

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testCreateOrUpdateIncident_successfulCreation() {
        Incident newIncident = new Incident();
        newIncident.setTitle(TEST_TITLE);
        newIncident.setDescription(TEST_DESCRIPTION);
        newIncident.setPriority(TEST_PRIORITY);
        newIncident.setStatus(TEST_STATUS);

        when(incidentRepository.findByTitleAndDescription(anyString(), anyString())).thenReturn(Optional.empty());
        when(incidentRepository.save(any(Incident.class))).thenReturn(newIncident);

        Incident result = incidentService.createOrUpdateIncident(
                TEST_TITLE, TEST_DESCRIPTION, TEST_PRIORITY, TEST_STATUS);

        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void testCreateOrUpdateIncident_duplicate() {
        Incident existingIncident = new Incident();
        existingIncident.setTitle(TEST_TITLE);
        existingIncident.setDescription(TEST_DESCRIPTION);

        when(incidentRepository.findByTitleAndDescription(TEST_TITLE, TEST_DESCRIPTION)).thenReturn(Optional.of(existingIncident));

        assertThrows(IllegalArgumentException.class, () -> {
            incidentService.createOrUpdateIncident(TEST_TITLE, TEST_DESCRIPTION, TEST_PRIORITY, TEST_STATUS);
        });

        verify(incidentRepository, never()).save(any(Incident.class));
    }

    @Test
    void testGetIncidentById_found() {
        Incident incident = new Incident();
        incident.setId(TEST_INCIDENT_ID);
        incident.setTitle(TEST_TITLE);

        when(incidentRepository.findById(TEST_INCIDENT_ID)).thenReturn(Optional.of(incident));

        Incident result = incidentService.getIncidentById(TEST_INCIDENT_ID);

        assertNotNull(result);
        assertEquals(TEST_INCIDENT_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
    }

    @Test
    void testGetIncidentById_notFound() {
        when(incidentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> incidentService.getIncidentById(TEST_INCIDENT_ID));
    }

    @Test
    void testGetAllIncidents() {
        Incident incident1 = new Incident();
        incident1.setTitle("Incident 1");

        Incident incident2 = new Incident();
        incident2.setTitle("Incident 2");

        when(incidentRepository.findAll()).thenReturn(Arrays.asList(incident1, incident2));

        List<Incident> result = incidentService.getAllIncidents();

        assertEquals(2, result.size());
        assertEquals("Incident 1", result.get(0).getTitle());
        assertEquals("Incident 2", result.get(1).getTitle());
    }

    @Test
    void testDeleteIncident() {
        // Act
        incidentService.deleteIncident(TEST_INCIDENT_ID);

        // Assert
        verify(incidentRepository, times(1)).deleteById(TEST_INCIDENT_ID);
    }

    @Test
    void testGetPaginatedIncidents() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        List<Incident> incidents = Arrays.asList(new Incident(), new Incident());
        Page<Incident> incidentPage = new PageImpl<>(incidents, pageRequest, 2);

        when(incidentRepository.findAll(pageRequest)).thenReturn(incidentPage);

        Page<Incident> result = incidentService.getPaginatedIncidents(0, 2);

        assertEquals(2, result.getContent().size());
        assertTrue(result.isLast());
        verify(incidentRepository, times(1)).findAll(pageRequest);
    }
}
