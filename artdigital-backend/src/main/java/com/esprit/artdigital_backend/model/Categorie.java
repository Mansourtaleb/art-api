package com.esprit.artdigital_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "categories")
public class Categorie {
    @Id
    private String id;

    private String nom;
    private String description;
    private String imageUrl;
    private Boolean actif = true;
    private Integer ordre = 0;

    public Categorie(String nom, String description, String imageUrl) {
        this.nom = nom;
        this.description = description;
        this.imageUrl = imageUrl;
        this.actif = true;
        this.ordre = 0;
    }
}