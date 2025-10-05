package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseLivraison {
    private String nom;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
    private Boolean parDefaut = false;
}