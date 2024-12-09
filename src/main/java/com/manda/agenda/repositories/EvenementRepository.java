package com.manda.agenda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manda.agenda.models.Evenement;

public interface EvenementRepository extends JpaRepository<Evenement, Integer> {

}
