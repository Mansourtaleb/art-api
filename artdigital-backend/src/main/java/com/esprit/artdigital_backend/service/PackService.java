package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.model.Pack;
import com.esprit.artdigital_backend.model.enums.Occasion;
import com.esprit.artdigital_backend.repository.PackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackService {

    private final PackRepository packRepository;

    // Créer un pack
    public Pack creerPack(Pack pack) {
        pack.setDateCreation(LocalDateTime.now());
        pack.setDateModification(LocalDateTime.now());
        pack.calculerPrixNormal();
        pack.calculerEconomie();
        return packRepository.save(pack);
    }

    // Récupérer tous les packs
    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    // Récupérer un pack par ID
    public Optional<Pack> getPackById(String id) {
        return packRepository.findById(id);
    }

    // Récupérer packs actifs
    public List<Pack> getPacksActifs() {
        return packRepository.findByActifTrue();
    }

    // Récupérer packs featured
    public List<Pack> getPacksFeatured() {
        return packRepository.findByFeaturedTrue();
    }

    // Récupérer packs par occasion
    public List<Pack> getPacksByOccasion(Occasion occasion) {
        return packRepository.findByOccasionAndActifTrue(occasion);
    }

    // Récupérer packs disponibles (dans période)
    public List<Pack> getPacksDisponibles() {
        LocalDateTime maintenant = LocalDateTime.now();
        return packRepository.findByActifTrueAndDateDebutBeforeAndDateFinAfter(maintenant, maintenant);
    }

    // Récupérer packs avec stock
    public List<Pack> getPacksAvecStock() {
        return packRepository.findByActifTrueAndStockGreaterThan(0);
    }

    // Récupérer packs en stock (alias pour getPacksAvecStock)
    public List<Pack> getPacksEnStock() {
        return getPacksAvecStock();
    }

    // Récupérer packs populaires
    public List<Pack> getPacksPopulaires() {
        return packRepository.findTop10ByActifTrueOrderByNombreVentesDesc();
    }

    // Rechercher packs par nom
    public List<Pack> rechercherPacks(String nom) {
        return packRepository.findByNomContainingIgnoreCase(nom);
    }

    // Récupérer packs triés par ordre
    public List<Pack> getPacksOrdonnes() {
        return packRepository.findByActifTrueOrderByOrdreAsc();
    }

    // Mettre à jour un pack
    public Pack updatePack(String id, Pack pack) {
        return packRepository.findById(id)
                .map(p -> {
                    p.setNom(pack.getNom());
                    p.setDescription(pack.getDescription());
                    p.setSlogan(pack.getSlogan());
                    p.setOccasion(pack.getOccasion());
                    p.setProduitsInclus(pack.getProduitsInclus());
                    p.setPrixPack(pack.getPrixPack());
                    p.setImagePrincipale(pack.getImagePrincipale());
                    p.setImagesSupplementaires(pack.getImagesSupplementaires());
                    p.setActif(pack.isActif());
                    p.setFeatured(pack.isFeatured());
                    p.setStock(pack.getStock());
                    p.setDateDebut(pack.getDateDebut());
                    p.setDateFin(pack.getDateFin());
                    p.setTags(pack.getTags());
                    p.setOrdre(pack.getOrdre());
                    p.setDateModification(LocalDateTime.now());
                    p.calculerPrixNormal();
                    p.calculerEconomie();
                    return packRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Pack non trouvé: " + id));
    }

    // Vendre un pack
    public boolean vendrePack(String packId, int quantite) {
        return packRepository.findById(packId)
                .map(p -> {
                    if (p.vendrePack(quantite)) {
                        packRepository.save(p);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    // Incrémenter vues
    public void incrementerVues(String packId) {
        packRepository.findById(packId)
                .ifPresent(p -> {
                    p.incrementerVues();
                    packRepository.save(p);
                });
    }

    // Incrémenter ajouts panier
    public void incrementerAjoutsPanier(String packId) {
        packRepository.findById(packId)
                .ifPresent(p -> {
                    p.incrementerAjoutsPanier();
                    packRepository.save(p);
                });
    }

    // Vérifier disponibilité
    public boolean verifierDisponibilite(String packId) {
        return packRepository.findById(packId)
                .map(Pack::estDisponible)
                .orElse(false);
    }

    // Toggle statut actif
    public Pack toggleActif(String packId) {
        return packRepository.findById(packId)
                .map(p -> {
                    p.setActif(!p.isActif());
                    p.setDateModification(LocalDateTime.now());
                    return packRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Pack non trouvé: " + packId));
    }

    // Supprimer un pack
    public void deletePack(String id) {
        packRepository.deleteById(id);
    }
}






