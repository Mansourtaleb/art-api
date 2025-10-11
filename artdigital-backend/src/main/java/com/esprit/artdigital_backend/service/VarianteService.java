package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.model.VarianteProduit;
import com.esprit.artdigital_backend.repository.VarianteProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VarianteService {

    private final VarianteProduitRepository varianteRepository;

    // Créer une variante
    public VarianteProduit creerVariante(VarianteProduit variante) {
        variante.setDateCreation(LocalDateTime.now());
        variante.setDateModification(LocalDateTime.now());
        return varianteRepository.save(variante);
    }

    // Récupérer toutes les variantes
    public List<VarianteProduit> getAllVariantes() {
        return varianteRepository.findAll();
    }

    // Récupérer une variante par ID
    public Optional<VarianteProduit> getVarianteById(String id) {
        return varianteRepository.findById(id);
    }

    // Récupérer variantes d'un produit
    public List<VarianteProduit> getVariantesByProduit(String produitId) {
        return varianteRepository.findByProduitId(produitId);
    }

    // Récupérer variantes disponibles d'un produit
    public List<VarianteProduit> getVariantesDisponibles(String produitId) {
        return varianteRepository.findByProduitIdAndDisponibleTrue(produitId);
    }

    // Récupérer variante par SKU
    public Optional<VarianteProduit> getVarianteBySku(String sku) {
        return varianteRepository.findBySku(sku);
    }

    // Récupérer variantes par couleur
    public List<VarianteProduit> getVariantesByCouleur(String produitId, String couleur) {
        return varianteRepository.findByProduitIdAndCouleur(produitId, couleur);
    }

    // Récupérer variantes avec stock faible
    public List<VarianteProduit> getVariantesStockFaible(Integer seuil) {
        return varianteRepository.findByStockLessThanEqualAndDisponibleTrue(seuil);
    }

    // Récupérer variantes featured
    public List<VarianteProduit> getVariantesFeatured() {
        return varianteRepository.findByFeaturedTrue();
    }

    // Récupérer variantes populaires
    public List<VarianteProduit> getVariantesPopulaires() {
        return varianteRepository.findTop10ByDisponibleTrueOrderByNombreVentesDesc();
    }

    // Mettre à jour une variante
    public VarianteProduit updateVariante(String id, VarianteProduit variante) {
        return varianteRepository.findById(id)
                .map(v -> {
                    v.setCouleur(variante.getCouleur());
                    v.setCodeColeurHex(variante.getCodeColeurHex());
                    v.setTaille(variante.getTaille());
                    v.setDimension(variante.getDimension());
                    v.setPrixSupplement(variante.getPrixSupplement());
                    v.setStock(variante.getStock());
                    v.setSeuilAlerte(variante.getSeuilAlerte());
                    v.setImageVariante(variante.getImageVariante());
                    v.setDisponible(variante.isDisponible());
                    v.setFeatured(variante.isFeatured());
                    v.setDateModification(LocalDateTime.now());
                    return varianteRepository.save(v);
                })
                .orElseThrow(() -> new RuntimeException("Variante non trouvée: " + id));
    }

    // Calculer prix final
    public BigDecimal calculerPrixFinal(String varianteId, BigDecimal prixBase) {
        return varianteRepository.findById(varianteId)
                .map(v -> v.calculerPrixFinal(prixBase))
                .orElse(prixBase);
    }

    // Réserver stock
    public boolean reserverStock(String varianteId, int quantite) {
        return varianteRepository.findById(varianteId)
                .map(v -> {
                    if (v.reserverStock(quantite)) {
                        varianteRepository.save(v);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    // Libérer stock
    public void libererStock(String varianteId, int quantite) {
        varianteRepository.findById(varianteId)
                .ifPresent(v -> {
                    v.libererStock(quantite);
                    varianteRepository.save(v);
                });
    }

    // Confirmer vente
    public boolean confirmerVente(String varianteId, int quantite) {
        return varianteRepository.findById(varianteId)
                .map(v -> {
                    if (v.confirmerVente(quantite)) {
                        varianteRepository.save(v);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    // Vérifier disponibilité
    public boolean verifierDisponibilite(String varianteId, int quantite) {
        return varianteRepository.findById(varianteId)
                .map(v -> v.getStockDisponible() >= quantite)
                .orElse(false);
    }

    // Mettre à jour le stock
    public VarianteProduit updateStock(String varianteId, Integer nouveauStock) {
        return varianteRepository.findById(varianteId)
                .map(v -> {
                    v.setStock(nouveauStock);
                    v.setDateModification(LocalDateTime.now());
                    return varianteRepository.save(v);
                })
                .orElseThrow(() -> new RuntimeException("Variante non trouvée: " + varianteId));
    }

    // Toggle disponibilité
    public VarianteProduit toggleDisponibilite(String varianteId) {
        return varianteRepository.findById(varianteId)
                .map(v -> {
                    v.setDisponible(!v.isDisponible());
                    v.setDateModification(LocalDateTime.now());
                    return varianteRepository.save(v);
                })
                .orElseThrow(() -> new RuntimeException("Variante non trouvée: " + varianteId));
    }

    // Supprimer une variante
    public void deleteVariante(String id) {
        varianteRepository.deleteById(id);
    }

    // Vérifier si SKU existe
    public boolean skuExists(String sku) {
        return varianteRepository.existsBySku(sku);
    }
}