package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.enums.StatutRetour;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "retours")
public class Retour {

    @Id
    private String id;

    private String commandeId;
    private String clientId;
    private String clientNom;
    private String clientEmail;

    private String motif;
    private String description;

    private StatutRetour statut = StatutRetour.DEMANDE;

    private String commentaireAdmin;

    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;

    private String produitsConcernes; // Liste des produits concernés

    public Retour(String commandeId, String clientId, String clientNom, String clientEmail,
                  String motif, String description, String produitsConcernes) {
        this.commandeId = commandeId;
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.clientEmail = clientEmail;
        this.motif = motif;
        this.description = description;
        this.produitsConcernes = produitsConcernes;
        this.statut = StatutRetour.DEMANDE;
        this.dateDemande = LocalDateTime.now();
    }

    public void changerStatut(StatutRetour nouveauStatut, String commentaire) {
        this.statut = nouveauStatut;
        this.commentaireAdmin = commentaire;
        this.dateTraitement = LocalDateTime.now();
    }
}