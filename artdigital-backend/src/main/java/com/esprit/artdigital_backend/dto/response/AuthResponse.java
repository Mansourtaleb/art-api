package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String userId;
    private String nom;
    private String email;
    private RoleUtilisateur role;
    private Boolean emailVerifie;
    public AuthResponse(String token, String userId, String nom, String email, RoleUtilisateur role) {
        this.token = token;
        this.userId = userId;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.emailVerifie = true;
    }
}