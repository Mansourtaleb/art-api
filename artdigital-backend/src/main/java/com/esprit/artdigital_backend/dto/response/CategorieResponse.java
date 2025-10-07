package com.esprit.artdigital_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieResponse {
    private String id;
    private String nom;
    private String description;
    private String imageUrl;
    private Boolean actif;
    private Integer ordre;
}