package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.model.enums.Genre;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponse {
    private String id;
    private String nom;
    private String email;
    private RoleUtilisateur role;
    private LocalDateTime dateInscription;
    private String photoProfile;
    private String telephone;
    private LocalDate dateNaissance;
    private Genre genre;
    private Boolean emailVerifie;
    private List<AdresseLivraison> adresses;
}