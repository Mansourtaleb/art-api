package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.enums.StatutProduitPersonnalise;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "produits_personnalises")
public class ProduitPersonnalise {
    @Id
    private String id;

    private String clientId;
    private String clientNom;
    private String typeProduit; // carte_visite, t_shirt, mug, etc.
    private String templateId; // ID du template utilisé
    private Map<String, String> personnalisations = new HashMap<>(); // clé: nom du champ, valeur: donnée client
    private String logoUrl; // URL du logo uploadé si applicable
    private BigDecimal prix;
    private StatutProduitPersonnalise statut = StatutProduitPersonnalise.EN_ATTENTE;
    private String previewUrl; // URL de l'aperçu généré
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String notes; // Notes de l'artiste/admin

    public ProduitPersonnalise(String clientId, String clientNom, String typeProduit,
                               String templateId, Map<String, String> personnalisations,
                               BigDecimal prix) {
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.typeProduit = typeProduit;
        this.templateId = templateId;
        this.personnalisations = personnalisations;
        this.prix = prix;
        this.statut = StatutProduitPersonnalise.EN_ATTENTE;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public void updateStatut(StatutProduitPersonnalise nouveauStatut) {
        this.statut = nouveauStatut;
        this.dateModification = LocalDateTime.now();
    }
}