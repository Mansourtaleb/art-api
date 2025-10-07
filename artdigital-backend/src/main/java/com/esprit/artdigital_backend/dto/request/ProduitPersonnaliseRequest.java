package com.esprit.artdigital_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitPersonnaliseRequest {

    @NotBlank(message = "Le type de produit est requis")
    private String typeProduit;

    @NotBlank(message = "L'ID du template est requis")
    private String templateId;

    @NotNull(message = "Les personnalisations sont requises")
    private Map<String, String> personnalisations;

    private String logoUrl;

    @NotNull(message = "Le prix est requis")
    private BigDecimal prix;

    private String notes;
}
