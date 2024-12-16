package com.manda.agenda.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manda.agenda.models.Evenement;

public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    @Query("SELECT e FROM Evenement e WHERE e.statut=:statut")
    List<Evenement> findByStatut(String statut);
}
