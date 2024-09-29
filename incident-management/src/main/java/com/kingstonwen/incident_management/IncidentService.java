package com.kingstonwen.incident_management;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Incident createOrUpdateIncident(String title, String description, IncidentPriority priority, IncidentStatus status) {
        // Check for duplicate incident by title and description
        Optional<Incident> existingIncident = incidentRepository.findByTitleAndDescription(title, description);

        if (existingIncident.isPresent()) {
            throw new IllegalArgumentException("Incident with the same title and description already exists.");
        }

        Incident incident = new Incident();
        incident.setTitle(title);
        incident.setDescription(description);
        incident.setPriority(priority);
        incident.setStatus(status);

        return incidentRepository.save(incident);
    }

    @CacheEvict(value = "incidentDetails", key = "#id")
    public Incident updateIncident(Long id, String title, String description, IncidentPriority priority, IncidentStatus status) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Incident not found with ID: " + id));

        incident.setTitle(title);
        incident.setDescription(description);
        incident.setPriority(priority);
        incident.setStatus(status);

        return incidentRepository.save(incident);
    }

    @CacheEvict(value = "incidentDetails", key = "#id")
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Page<Incident> getPaginatedIncidents(int page, int size) {
        return incidentRepository.findAll(PageRequest.of(page, size));
    }

    @Cacheable("incidentDetails")
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Incident not found with ID: " + id));
    }
}

