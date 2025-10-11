package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Classe représentant un produit inclus dans un pack thématique
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitPack {

    private String produitId;
    private String nomProduit;
    private String varianteId;
    private String descriptionVariante;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private String imageProduit;
    private Integer ordre;

    public ProduitPack(String produitId, String nomProduit, Integer quantite, BigDecimal prixUnitaire) {
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.ordre = 0;
    }

    public BigDecimal calculerPrixTotal() {
        if (prixUnitaire == null || quantite == null) {
            return BigDecimal.ZERO;
        }
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }
}
