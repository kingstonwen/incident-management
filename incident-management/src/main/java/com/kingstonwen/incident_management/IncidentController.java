package com.kingstonwen.incident_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        try {
            Incident createdIncident = incidentService.createOrUpdateIncident(
                    incident.getTitle(),
                    incident.getDescription(),
                    incident.getPriority(),
                    incident.getStatus()
            );
            return new ResponseEntity<>(createdIncident, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Incident>> getAllIncidentsWithPagination(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Incident> incidentPage = incidentService.getPaginatedIncidents(page, size);
        return new ResponseEntity<>(incidentPage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return new ResponseEntity<>(incidentService.getAllIncidents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        try {
            Incident incident = incidentService.getIncidentById(id);
            return new ResponseEntity<>(incident, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        try {
            Incident updatedIncident = incidentService.updateIncident(
                    id,
                    incident.getTitle(),
                    incident.getDescription(),
                    incident.getPriority(),
                    incident.getStatus()
            );
            return new ResponseEntity<>(updatedIncident, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
