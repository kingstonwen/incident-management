package com.kingstonwen.incident_management;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

   @Query("SELECT i FROM Incident i WHERE i.title = :title AND i.description = :description")
   Optional<Incident> findByTitleAndDescription(@Param("title") String title, @Param("description") String description);

}

