package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierItem {
    private String oeuvreId;
    private String titre;
    private BigDecimal prixUnitaire;
    private Integer quantite;
    private String imageUrl;
    private Integer stockDisponible;

    // Pour personnalisation simple (Phase 1)
    private String notePersonnalisation;
    private String imagePersonnalisationUrl;

    public BigDecimal calculerSousTotal() {
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }
}