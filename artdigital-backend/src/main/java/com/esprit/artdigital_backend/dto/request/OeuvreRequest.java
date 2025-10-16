package com.esprit.artdigital_backend.dto.request;

import com.esprit.artdigital_backend.model.enums.StatutOeuvre;
import jakarta.validation.constraints.*;
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

    private String description;

    @NotBlank(message = "La catégorie est requise")
    private String categorieId; // ← IMPORTANT : Recevoir l'ID

    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal prix;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantiteDisponible = 1;

    private List<String> images;

    private StatutOeuvre statut = StatutOeuvre.PUBLIE;

    // Ancienne propriété pour compatibilité (optionnel)
    private String categorie;
}