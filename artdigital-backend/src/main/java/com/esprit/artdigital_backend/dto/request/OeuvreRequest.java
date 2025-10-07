package com.esprit.artdigital_backend.dto.request;

import com.esprit.artdigital_backend.model.enums.StatutOeuvre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OeuvreRequest {
    @NotBlank(message = "Le titre est requis")
    private String titre;

    @NotBlank(message = "La description est requise")
    private String description;

    @NotBlank(message = "La catégorie est requise")
    private String categorie;

    @NotNull(message = "Le prix est requis")
    @Min(value = 0, message = "Le prix doit être positif")
    private BigDecimal prix;

    @NotNull(message = "La quantité est requise")
    @Min(value = 0, message = "La quantité doit être positive")
    private Integer quantiteDisponible;

    private List<String> images;
    private StatutOeuvre statut;
}