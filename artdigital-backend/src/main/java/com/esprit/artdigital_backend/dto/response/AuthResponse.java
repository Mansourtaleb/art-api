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
}