package com.esprit.artdigital_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesDTO {

    // Métriques principales
    private BigDecimal chiffreAffairesTotal;
    private BigDecimal chiffreAffairesMois;
    private BigDecimal chiffreAffairesSemaine;
    private BigDecimal chiffreAffairesJour;

    private Long nombreCommandesTotal;
    private Long nombreCommandesMois;
    private Long nombreCommandesSemaine;
    private Long nombreCommandesJour;

    private Long nombreClients;
    private Long nombreNouveauxClientsMois;

    private Long nombreProduitsVendus;
    private Long nombreProduitsEnStock;
    private Long nombreProduitsStockFaible;

    // Commandes par statut
    private Map<String, Long> commandesParStatut;

    // Top produits
    private List<ProduitVenteDTO> topProduits;

    // Revenus par catégorie
    private Map<String, BigDecimal> revenusParCategorie;

    // Évolution CA (7 derniers jours)
    private List<ChiffreAffairesJourDTO> evolutionCA;

    // Statistiques retours
    private Long nombreRetours;
    private Long nombreRetoursEnAttente;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitVenteDTO {
        private String produitId;
        private String produitTitre;
        private Long quantiteVendue;
        private BigDecimal montantTotal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiffreAffairesJourDTO {
        private String date;
        private BigDecimal montant;
        private Long nombreCommandes;
    }
}