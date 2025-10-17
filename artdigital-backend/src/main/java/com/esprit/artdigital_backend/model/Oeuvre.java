package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.embedded.AvisOeuvre;
import com.esprit.artdigital_backend.model.enums.StatutOeuvre;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "oeuvres")
public class Oeuvre {
    @Id
    private String id;

    private String titre;
    private String description;
    private String categorie;
    private BigDecimal prix;
    private Integer quantiteDisponible;
    private String artisteId;
    private String artisteNom;
    private List<String> images = new ArrayList<>();
    private StatutOeuvre statut = StatutOeuvre.DISPONIBLE;
    private LocalDateTime dateCreation;
    private List<AvisOeuvre> avis = new ArrayList<>();
    private Double notemoyenne;

    // ❌ SUPPRIMER CETTE LIGNE (causait le bug)
    // @DBRef
    // private Categorie categorieRef;

    public Oeuvre(String titre, String description, String categorie, BigDecimal prix,
                  Integer quantiteDisponible, String artisteId, String artisteNom) {
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.quantiteDisponible = quantiteDisponible;
        this.artisteId = artisteId;
        this.artisteNom = artisteNom;
        this.dateCreation = LocalDateTime.now();
        this.statut = StatutOeuvre.DISPONIBLE;
    }

    public void ajouterAvis(AvisOeuvre avis) {
        if (this.avis == null) {
            this.avis = new ArrayList<>();
        }
        this.avis.add(avis);
        calculerNoteMoyenne();
    }

    private void calculerNoteMoyenne() {
        if (this.avis == null || this.avis.isEmpty()) {
            this.notemoyenne = null;
            return;
        }
        double moyenne = this.avis.stream()
                .mapToInt(AvisOeuvre::getNote)
                .average()
                .orElse(0.0);
        this.notemoyenne = Math.round(moyenne * 10.0) / 10.0;
    }
}