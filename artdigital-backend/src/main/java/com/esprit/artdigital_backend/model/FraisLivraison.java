package com.esprit.artdigital_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant les frais de livraison par zone/ville
 */
@Document(collection = "frais_livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraisLivraison {

    @Id
    private String id;

    @Indexed
    private String zone;

    private String typeZone;
    private List<String> villesIncluses;
    private String codePostal;

    private BigDecimal frais;
    private BigDecimal montantLivraisonGratuite;
    private BigDecimal fraisExpress;
    private BigDecimal supplementWeekend;

    private Integer delaiJours;
    private Integer delaiMinimum;
    private Integer delaiMaximum;
    private Integer delaiExpress;

    private boolean actif;
    private boolean expressDisponible;
    private boolean weekendDisponible;
    private Integer priorite;

    private Integer nombreLivraisons;
    private LocalDateTime derniereLivraison;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String modifieParAdmin;

    private String notes;

    public BigDecimal calculerFrais(BigDecimal montantCommande) {
        if (montantLivraisonGratuite != null &&
                montantCommande.compareTo(montantLivraisonGratuite) >= 0) {
            return BigDecimal.ZERO;
        }

        return frais;
    }

    public BigDecimal calculerFraisExpress(BigDecimal montantCommande) {
        if (!expressDisponible || fraisExpress == null) {
            return null;
        }

        if (montantLivraisonGratuite != null &&
                montantCommande.compareTo(montantLivraisonGratuite) >= 0) {
            return BigDecimal.ZERO;
        }

        return fraisExpress;
    }

    public boolean estLivraisonGratuite(BigDecimal montantCommande) {
        return montantLivraisonGratuite != null &&
                montantCommande.compareTo(montantLivraisonGratuite) >= 0;
    }

    public BigDecimal montantRestantPourGratuit(BigDecimal montantCommande) {
        if (montantLivraisonGratuite == null) {
            return null;
        }

        if (estLivraisonGratuite(montantCommande)) {
            return BigDecimal.ZERO;
        }

        return montantLivraisonGratuite.subtract(montantCommande);
    }

    public boolean contientVille(String ville) {
        if (ville == null) {
            return false;
        }

        String villeLower = ville.toLowerCase().trim();

        if (zone != null && zone.toLowerCase().trim().equals(villeLower)) {
            return true;
        }

        if (villesIncluses != null) {
            return villesIncluses.stream()
                    .anyMatch(v -> v.toLowerCase().trim().equals(villeLower));
        }

        return false;
    }

    public void ajouterVille(String ville) {
        if (this.villesIncluses == null) {
            this.villesIncluses = new ArrayList<>();
        }
        if (!contientVille(ville)) {
            this.villesIncluses.add(ville);
        }
    }

    public void retirerVille(String ville) {
        if (this.villesIncluses != null) {
            this.villesIncluses.removeIf(v -> v.equalsIgnoreCase(ville));
        }
    }

    public void enregistrerLivraison() {
        this.nombreLivraisons = (this.nombreLivraisons != null ? this.nombreLivraisons : 0) + 1;
        this.derniereLivraison = LocalDateTime.now();
    }

    public String getDescriptionFrais() {
        StringBuilder sb = new StringBuilder();

        if (frais.compareTo(BigDecimal.ZERO) == 0) {
            sb.append("Livraison gratuite");
        } else {
            sb.append(String.format("%.3f TND", frais));
        }

        if (montantLivraisonGratuite != null) {
            sb.append(String.format(" (Gratuit à partir de %.3f TND)", montantLivraisonGratuite));
        }

        if (delaiJours != null) {
            sb.append(String.format(" - Délai: %d jours", delaiJours));
        }

        return sb.toString();
    }

    public String getDelaiTexte() {
        if (delaiMinimum != null && delaiMaximum != null) {
            return String.format("%d-%d jours", delaiMinimum, delaiMaximum);
        } else if (delaiJours != null) {
            return String.format("%d jours", delaiJours);
        } else {
            return "Délai à confirmer";
        }
    }

    public int compareTo(FraisLivraison autre) {
        if (this.priorite == null && autre.priorite == null) {
            return 0;
        }
        if (this.priorite == null) {
            return 1;
        }
        if (autre.priorite == null) {
            return -1;
        }
        return autre.priorite.compareTo(this.priorite);
    }
}
