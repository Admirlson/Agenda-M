package com.manda.agenda.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    @NotEmpty(message = "Last name not be empty")
    private String lastName;

    @NotEmpty(message = "First name not be empty")
    private String firstName;

    @NotEmpty(message = "Username not be empty")
    private String username;

    @NotEmpty(message = "Password not be empty")
    private String password;

    private String role;

    @NotEmpty(message = "Who created not be empty")
    private String whoCreated;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    private String whoModified;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateModified;

    private String firstConnection;

    private String statut;

}
