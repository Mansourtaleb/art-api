package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.model.embedded.ProduitCommande;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponse {
    private String id;
    private String clientId;
    private String clientNom;
    private List<ProduitCommande> produits;
    private BigDecimal montantTotal;
    private AdresseLivraison adresseLivraison;
    private StatutCommande statut;
    private LocalDateTime dateCommande;
    private LocalDateTime dateModification;
}