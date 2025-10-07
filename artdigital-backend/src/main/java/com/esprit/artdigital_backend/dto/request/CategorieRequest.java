package com.esprit.artdigital_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieRequest {
    @NotBlank(message = "Le nom est requis")
    private String nom;

    private String description;
    private String imageUrl;
    private Boolean actif;
    private Integer ordre;
}