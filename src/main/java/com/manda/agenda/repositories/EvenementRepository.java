package com.manda.agenda.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manda.agenda.models.Evenement;

import jakarta.transaction.Transactional;

public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    @Query("SELECT e FROM Evenement e WHERE e.statut=:statut")
    List<Evenement> findByStatut(String statut);

    @Modifying
    @Transactional
    @Query("Update Evenement e set e.remimderSent= :remimderSent WHERE e.id= :id")
    int updateRemimderSent(@Param("remimderSent") boolean remimderSent, @Param("id") int id);

}
