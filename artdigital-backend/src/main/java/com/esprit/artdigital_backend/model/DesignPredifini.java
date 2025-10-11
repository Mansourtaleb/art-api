package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.enums.CategorieDesign;
import com.esprit.artdigital_backend.model.enums.TypeProduit;
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
 * Entité représentant un design prédéfini dans la bibliothèque
 */
@Document(collection = "designs_predefinis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignPredifini {

    @Id
    private String id;

    private String nom;
    private String description;
    private CategorieDesign categorie;
    private String sousCategorie;

    private List<ImageDesign> images;
    private String imageMiniature;

    private List<TypeProduit> produitsCompatibles;
    private List<String> taillesRecommandees;
    private List<String> zonesApplicables;

    private List<String> tags;
    private List<String> couleursDominantes;
    private String style;

    private boolean contientTexte;
    private String texte;
    private String langue;

    private boolean gratuit;
    private BigDecimal prixSupplement;

    private boolean actif;
    private boolean featured;
    private boolean exclusif;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    private Integer nombreUtilisations;
    private Integer nombreVues;
    private Integer nombreFavoris;
    private Double noteMoyenne;
    private Integer nombreAvis;
    private Double scorePopularite;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String creeParAdmin;

    private String artiste;
    private String licence;
    private Integer ordre;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDesign {
        private String url;
        private String variante;
        private String description;
        private String tailleFichier;
        private String resolution;
    }

    public boolean estCompatibleAvec(TypeProduit typeProduit) {
        return produitsCompatibles != null && produitsCompatibles.contains(typeProduit);
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

        return true;
    }

    public void ajouterImage(String url, String variante, String description) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(new ImageDesign(url, variante, description, null, null));
    }

    public void ajouterTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag.toLowerCase())) {
            this.tags.add(tag.toLowerCase());
        }
    }

    public void incrementerUtilisations() {
        this.nombreUtilisations = (this.nombreUtilisations != null ? this.nombreUtilisations : 0) + 1;
        calculerPopularite();
    }

    public void incrementerVues() {
        this.nombreVues = (this.nombreVues != null ? this.nombreVues : 0) + 1;
        calculerPopularite();
    }

    public void incrementerFavoris() {
        this.nombreFavoris = (this.nombreFavoris != null ? this.nombreFavoris : 0) + 1;
        calculerPopularite();
    }

    public void calculerPopularite() {
        int util = (nombreUtilisations != null) ? nombreUtilisations : 0;
        int vues = (nombreVues != null) ? nombreVues : 0;
        int fav = (nombreFavoris != null) ? nombreFavoris : 0;
        double note = (noteMoyenne != null) ? noteMoyenne : 0;

        this.scorePopularite = (util * 5.0) + (vues * 0.1) + (fav * 2.0) + (note * 10.0);
    }

    public boolean contientMotCle(String motCle) {
        String recherche = motCle.toLowerCase();

        if (nom != null && nom.toLowerCase().contains(recherche)) {
            return true;
        }

        if (description != null && description.toLowerCase().contains(recherche)) {
            return true;
        }

        if (texte != null && texte.toLowerCase().contains(recherche)) {
            return true;
        }

        if (tags != null && tags.stream().anyMatch(t -> t.contains(recherche))) {
            return true;
        }

        return false;
    }

    public String getImageDefaut() {
        if (imageMiniature != null) {
            return imageMiniature;
        }

        if (images != null && !images.isEmpty()) {
            return images.get(0).getUrl();
        }

        return null;
    }

    public String getImageVariante(String variante) {
        if (images == null) {
            return getImageDefaut();
        }

        return images.stream()
                .filter(img -> variante.equalsIgnoreCase(img.getVariante()))
                .map(ImageDesign::getUrl)
                .findFirst()
                .orElse(getImageDefaut());
    }
}
