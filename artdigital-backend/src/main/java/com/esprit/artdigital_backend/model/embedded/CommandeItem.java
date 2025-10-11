package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Classe représentant un article dans une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeItem {

    private String produitId;
    private String nomProduit;
    private String typeProduit;
    private String varianteId;
    private String couleur;
    private String taille;
    private String dimension;
    private String sku;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal prixTotal;
    private PersonnalisationClient personnalisation;
    private String imageProduit;

    public CommandeItem(String produitId, String nomProduit, String typeProduit,
                        String varianteId, Integer quantite, BigDecimal prixUnitaire) {
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.typeProduit = typeProduit;
        this.varianteId = varianteId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public CommandeItem(String produitId, String nomProduit, String typeProduit,
                        String varianteId, Integer quantite, BigDecimal prixUnitaire,
                        PersonnalisationClient personnalisation) {
        this(produitId, nomProduit, typeProduit, varianteId, quantite, prixUnitaire);
        this.personnalisation = personnalisation;
    }

    public BigDecimal calculerPrixTotal() {
        if (prixUnitaire == null || quantite == null) {
            return BigDecimal.ZERO;
        }
        this.prixTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        return this.prixTotal;
    }

    public boolean estPersonnalise() {
        return personnalisation != null && personnalisation.aPersonnalisation();
    }

    public String getDescriptionComplete() {
        StringBuilder sb = new StringBuilder();
        sb.append(nomProduit);

        if (couleur != null) {
            sb.append(" - ").append(couleur);
        }
        if (taille != null) {
            sb.append(" - ").append(taille);
        }
        if (dimension != null) {
            sb.append(" - ").append(dimension);
        }
        if (estPersonnalise()) {
            sb.append(" (Personnalisé)");
        }

        return sb.toString();
    }
}
