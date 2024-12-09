package com.manda.agenda.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // @Column(name = "lastname")
    @NotEmpty(message = "Last name not be empty")
    private String lastName;

    // @Column(name = "firstname")
    @NotEmpty(message = "First name not be empty")
    private String firstName;

    // @Column(name = "username")
    @NotEmpty(message = "Username not be empty")
    @Column(unique = true)
    private String username;

    // @Column(name = "password")
    private String password;

    // @Column(name = "role")
    private String role;

    // @Column(name = "whocreated")
    private String whoCreated;

    // @Temporal(TemporalType.DATE)
    // @Column(name = "datecreated")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;

    // @Column(name = "whomodified")
    private String whoModified;

    // @Temporal(TemporalType.DATE)
    // @Column(name = "datemodified")
    private LocalDate dateModified;

    private String firstConnection;

    private String statut;

}
