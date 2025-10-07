package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.model.embedded.ProduitCommande;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
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
@Document(collection = "commandes")
public class Commande {
    @Id
    private String id;

    private String clientId;
    private String clientNom;
    private List<ProduitCommande> produits = new ArrayList<>();
    private BigDecimal montantTotal;
    private AdresseLivraison adresseLivraison;
    private StatutCommande statut = StatutCommande.EN_ATTENTE;
    private LocalDateTime dateCommande;
    private LocalDateTime dateModification;

    public Commande(String clientId, String clientNom, List<ProduitCommande> produits,
                    BigDecimal montantTotal, AdresseLivraison adresseLivraison) {
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.produits = produits;
        this.montantTotal = montantTotal;
        this.adresseLivraison = adresseLivraison;
        this.statut = StatutCommande.EN_ATTENTE;
        this.dateCommande = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public void updateStatut(StatutCommande nouveauStatut) {
        this.statut = nouveauStatut;
        this.dateModification = LocalDateTime.now();
    }
}