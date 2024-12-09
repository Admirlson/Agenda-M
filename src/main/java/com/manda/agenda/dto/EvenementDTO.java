package com.manda.agenda.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvenementDTO {
    private Integer id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    private String format;

    private String type;

    private String heure;

    private String institution;

    private String objectif;

    private String statut;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate nouvelleDate;

    private String suivis;

    private String whocreated;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate datecreated;

    private String whomodified;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate datemodified;

    private boolean reminderSent;
}
