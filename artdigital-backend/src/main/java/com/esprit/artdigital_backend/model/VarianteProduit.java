package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.enums.TailleVetement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant une variante d'un produit
 */
@Document(collection = "variantes_produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProduit {

    @Id
    private String id;

    @DBRef
    private Oeuvre produit;

    @Indexed(unique = true)
    private String sku;

    private String couleur;
    private String codeColeurHex;
    private TailleVetement taille;
    private String dimension;

    private BigDecimal prixSupplement;
    private BigDecimal prixFinal;

    private Integer stock;
    private Integer seuilAlerte;
    private Integer stockReserve;

    private String imageVariante;
    private java.util.List<String> imagesSupplementaires;

    private boolean disponible;
    private boolean featured;

    private Integer nombreVentes;
    private Integer nombreAjoutsPanier;
    private Integer nombreAjoutsWishlist;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String creeParAdmin;

    public BigDecimal calculerPrixFinal(BigDecimal prixBase) {
        if (prixSupplement == null) {
            this.prixFinal = prixBase;
        } else {
            this.prixFinal = prixBase.add(prixSupplement);
        }
        return this.prixFinal;
    }

    public Integer getStockDisponible() {
        int reserve = (stockReserve != null) ? stockReserve : 0;
        int stockActuel = (stock != null) ? stock : 0;
        return Math.max(0, stockActuel - reserve);
    }

    public boolean estStockFaible() {
        if (stock == null || seuilAlerte == null) {
            return false;
        }
        return stock <= seuilAlerte;
    }

    public boolean estEnRupture() {
        return getStockDisponible() <= 0;
    }

    public boolean reserverStock(int quantite) {
        if (getStockDisponible() < quantite) {
            return false;
        }
        this.stockReserve = (this.stockReserve != null ? this.stockReserve : 0) + quantite;
        return true;
    }

    public void libererStock(int quantite) {
        if (this.stockReserve != null) {
            this.stockReserve = Math.max(0, this.stockReserve - quantite);
        }
    }

    public boolean confirmerVente(int quantite) {
        if (getStockDisponible() < quantite) {
            return false;
        }

        this.stock -= quantite;

        if (this.stockReserve != null) {
            this.stockReserve = Math.max(0, this.stockReserve - quantite);
        }

        this.nombreVentes = (this.nombreVentes != null ? this.nombreVentes : 0) + quantite;

        return true;
    }

    public String getDescriptionComplete() {
        StringBuilder sb = new StringBuilder();

        if (couleur != null) {
            sb.append(couleur);
        }
        if (taille != null) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(taille.getLibelle());
        }
        if (dimension != null) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(dimension);
        }

        return sb.toString();
    }

    public static String genererSKU(String nomProduit, String couleur, String taille, String dimension) {
        StringBuilder sku = new StringBuilder();

        if (nomProduit != null) {
            String[] mots = nomProduit.split(" ");
            for (String mot : mots) {
                if (!mot.isEmpty()) {
                    sku.append(mot.substring(0, 1).toUpperCase());
                }
            }
        }

        sku.append("-");

        if (couleur != null) {
            sku.append(couleur.toUpperCase()).append("-");
        }

        if (taille != null) {
            sku.append(taille.toUpperCase()).append("-");
        }

        if (dimension != null) {
            sku.append(dimension.toUpperCase().replace(" ", "")).append("-");
        }

        sku.append(System.currentTimeMillis());

        return sku.toString();
    }
}
