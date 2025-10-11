package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.embedded.ProduitPack;
import com.esprit.artdigital_backend.model.enums.Occasion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un pack/bundle de produits thématiques
 */
@Document(collection = "packs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pack {

    @Id
    private String id;

    private String nom;
    private String description;
    private String slogan;
    private Occasion occasion;

    private List<ProduitPack> produitsInclus;
    private Integer nombreProduits;

    private BigDecimal prixNormal;
    private BigDecimal prixPack;
    private BigDecimal economie;
    private Double pourcentageReduction;

    private String imagePrincipale;
    private List<String> imagesSupplementaires;
    private String urlVideo;

    private boolean actif;
    private boolean featured;
    private Integer stock;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    private Integer nombreVentes;
    private Integer nombreVues;
    private Integer nombreAjoutsPanier;
    private Double noteMoyenne;
    private Integer nombreAvis;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String creeParAdmin;

    private List<String> tags;
    private Integer ordre;

    public BigDecimal calculerPrixNormal() {
        if (produitsInclus == null || produitsInclus.isEmpty()) {
            this.prixNormal = BigDecimal.ZERO;
            return this.prixNormal;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (ProduitPack produit : produitsInclus) {
            total = total.add(produit.calculerPrixTotal());
        }

        this.prixNormal = total;
        this.nombreProduits = produitsInclus.size();
        return total;
    }

    public void calculerEconomie() {
        if (prixNormal != null && prixPack != null) {
            this.economie = prixNormal.subtract(prixPack);

            if (prixNormal.compareTo(BigDecimal.ZERO) > 0) {
                double pourcent = economie.divide(prixNormal, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
                this.pourcentageReduction = Math.round(pourcent * 100.0) / 100.0;
            }
        }
    }

    public boolean estDisponible() {
        if (!actif) {
            return false;
        }

        LocalDateTime maintenant = LocalDateTime.now();

        if (dateDebut != null && maintenant.isBefore(dateDebut)) {
            return false;
        }

        if (dateFin != null && maintenant.isAfter(dateFin)) {
            return false;
        }

        if (stock != null && stock <= 0) {
            return false;
        }

        return true;
    }

    public boolean estPromotionLimitee() {
        return dateFin != null;
    }

    public Long getJoursRestants() {
        if (dateFin == null) {
            return null;
        }

        LocalDateTime maintenant = LocalDateTime.now();
        if (maintenant.isAfter(dateFin)) {
            return 0L;
        }

        return java.time.Duration.between(maintenant, dateFin).toDays();
    }

    public void ajouterProduit(ProduitPack produit) {
        if (this.produitsInclus == null) {
            this.produitsInclus = new ArrayList<>();
        }
        this.produitsInclus.add(produit);
        calculerPrixNormal();
    }

    public void retirerProduit(String produitId) {
        if (this.produitsInclus != null) {
            this.produitsInclus.removeIf(p -> p.getProduitId().equals(produitId));
            calculerPrixNormal();
        }
    }

    public boolean contientProduit(String produitId) {
        if (produitsInclus == null) {
            return false;
        }
        return produitsInclus.stream()
                .anyMatch(p -> p.getProduitId().equals(produitId));
    }

    public boolean vendrePack(int quantite) {
        if (stock != null) {
            if (stock < quantite) {
                return false;
            }
            this.stock -= quantite;
        }

        this.nombreVentes = (this.nombreVentes != null ? this.nombreVentes : 0) + quantite;
        return true;
    }

    public void incrementerVues() {
        this.nombreVues = (this.nombreVues != null ? this.nombreVues : 0) + 1;
    }

    public void incrementerAjoutsPanier() {
        this.nombreAjoutsPanier = (this.nombreAjoutsPanier != null ? this.nombreAjoutsPanier : 0) + 1;
    }

    public String getResume() {
        return String.format("%s - %d produits - %.3f TND (au lieu de %.3f TND)",
                nom,
                nombreProduits,
                prixPack,
                prixNormal);
    }
}
