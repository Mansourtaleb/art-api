package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.model.DesignPredifini;
import com.esprit.artdigital_backend.model.enums.CategorieDesign;
import com.esprit.artdigital_backend.model.enums.TypeProduit;
import com.esprit.artdigital_backend.repository.DesignPredifiniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesignService {

    private final DesignPredifiniRepository designRepository;

    // Créer un design
    public DesignPredifini creerDesign(DesignPredifini design) {
        design.setDateCreation(LocalDateTime.now());
        design.setDateModification(LocalDateTime.now());
        design.calculerPopularite();
        return designRepository.save(design);
    }

    // Récupérer tous les designs
    public List<DesignPredifini> getAllDesigns() {
        return designRepository.findAll();
    }

    // Récupérer un design par ID
    public Optional<DesignPredifini> getDesignById(String id) {
        return designRepository.findById(id);
    }

    // Récupérer designs actifs
    public List<DesignPredifini> getDesignsActifs() {
        return designRepository.findByActifTrue();
    }

    // Récupérer designs featured
    public List<DesignPredifini> getDesignsFeatured() {
        return designRepository.findByFeaturedTrue();
    }

    // Récupérer designs gratuits
    public List<DesignPredifini> getDesignsGratuits() {
        return designRepository.findByGratuitTrueAndActifTrue();
    }

    // Récupérer designs premium
    public List<DesignPredifini> getDesignsPremium() {
        return designRepository.findByGratuitFalseAndActifTrue();
    }

    // Récupérer designs par catégorie
    public List<DesignPredifini> getDesignsByCategorie(CategorieDesign categorie) {
        return designRepository.findByCategorieAndActifTrue(categorie);
    }

    // Récupérer designs compatibles avec un produit
    public List<DesignPredifini> getDesignsCompatibles(TypeProduit typeProduit) {
        return designRepository.findByProduitsCompatiblesContainingAndActifTrue(typeProduit);
    }

    // Récupérer designs populaires
    public List<DesignPredifini> getDesignsPopulaires() {
        return designRepository.findTop20ByActifTrueOrderByScorePopulariteDesc();
    }

    // Récupérer designs par tag
    public List<DesignPredifini> getDesignsByTag(String tag) {
        return designRepository.findByTagsContainingAndActifTrue(tag);
    }

    // Rechercher designs par nom
    public List<DesignPredifini> rechercherDesigns(String nom) {
        return designRepository.findByNomContainingIgnoreCaseAndActifTrue(nom);
    }

    // Récupérer designs récents
    public List<DesignPredifini> getDesignsRecents() {
        return designRepository.findTop20ByActifTrueOrderByDateCreationDesc();
    }

    // Récupérer designs triés par ordre
    public List<DesignPredifini> getDesignsOrdonnes() {
        return designRepository.findByActifTrueOrderByOrdreAsc();
    }

    // Mettre à jour un design
    public DesignPredifini updateDesign(String id, DesignPredifini design) {
        return designRepository.findById(id)
                .map(d -> {
                    d.setNom(design.getNom());
                    d.setDescription(design.getDescription());
                    d.setCategorie(design.getCategorie());
                    d.setSousCategorie(design.getSousCategorie());
                    d.setImages(design.getImages());
                    d.setImageMiniature(design.getImageMiniature());
                    d.setProduitsCompatibles(design.getProduitsCompatibles());
                    d.setTags(design.getTags());
                    d.setCouleursDominantes(design.getCouleursDominantes());
                    d.setStyle(design.getStyle());
                    d.setContientTexte(design.isContientTexte());
                    d.setTexte(design.getTexte());
                    d.setLangue(design.getLangue());
                    d.setGratuit(design.isGratuit());
                    d.setPrixSupplement(design.getPrixSupplement());
                    d.setActif(design.isActif());
                    d.setFeatured(design.isFeatured());
                    d.setExclusif(design.isExclusif());
                    d.setDateModification(LocalDateTime.now());
                    return designRepository.save(d);
                })
                .orElseThrow(() -> new RuntimeException("Design non trouvé: " + id));
    }

    // Incrémenter utilisations
    public void incrementerUtilisations(String designId) {
        designRepository.findById(designId)
                .ifPresent(d -> {
                    d.incrementerUtilisations();
                    designRepository.save(d);
                });
    }

    // Incrémenter vues
    public void incrementerVues(String designId) {
        designRepository.findById(designId)
                .ifPresent(d -> {
                    d.incrementerVues();
                    designRepository.save(d);
                });
    }

    // Incrémenter favoris
    public void incrementerFavoris(String designId) {
        designRepository.findById(designId)
                .ifPresent(d -> {
                    d.incrementerFavoris();
                    designRepository.save(d);
                });
    }

    // Vérifier compatibilité
    public boolean verifierCompatibilite(String designId, TypeProduit typeProduit) {
        return designRepository.findById(designId)
                .map(d -> d.estCompatibleAvec(typeProduit))
                .orElse(false);
    }

    // Vérifier disponibilité
    public boolean verifierDisponibilite(String designId) {
        return designRepository.findById(designId)
                .map(DesignPredifini::estDisponible)
                .orElse(false);
    }

    // Supprimer un design
    public void deleteDesign(String id) {
        designRepository.deleteById(id);
    }
}






