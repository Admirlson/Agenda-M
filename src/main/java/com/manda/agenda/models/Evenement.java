package com.manda.agenda.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private LocalDate date;
    private String format;
    private String type;
    private String heure;
    private String institution;
    private String objectif;
    private String statut;
    private LocalDate nouvelleDate;
    private String suivis;
    private String whocreated;
    private LocalDate datecreated;
    private String whomodified;
    private LocalDate datemodified;
    private boolean remimderSent;
}
