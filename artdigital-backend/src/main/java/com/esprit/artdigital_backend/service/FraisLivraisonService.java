package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.model.FraisLivraison;
import com.esprit.artdigital_backend.repository.FraisLivraisonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FraisLivraisonService {

    private final FraisLivraisonRepository fraisRepository;

    public FraisLivraison creerFrais(FraisLivraison frais) {
        frais.setDateCreation(LocalDateTime.now());
        frais.setActif(true);
        return fraisRepository.save(frais);
    }

    public Optional<FraisLivraison> getFraisById(String id) {
        return fraisRepository.findById(id);
    }

    public Optional<FraisLivraison> getFraisByZone(String zone) {
        return fraisRepository.findByZoneIgnoreCase(zone);
    }

    public List<FraisLivraison> getAllFrais() {
        return fraisRepository.findAll();
    }

    public List<FraisLivraison> getFraisActifs() {
        return fraisRepository.findByActifTrueOrderByPrioriteDesc();
    }

    public List<FraisLivraison> getFraisExpressDisponibles() {
        return fraisRepository.findByActifTrueAndExpressDisponibleTrue();
    }

    public List<FraisLivraison> getFraisAvecLivraisonGratuite() {
        return fraisRepository.findByActifTrueAndMontantLivraisonGratuiteIsNotNull();
    }

    public BigDecimal calculerFrais(String ville, BigDecimal montantCommande) {
        Optional<FraisLivraison> frais = fraisRepository.findByZoneIgnoreCase(ville);

        if (frais.isEmpty()) {
            List<FraisLivraison> allFrais = fraisRepository.findByActifTrue();
            for (FraisLivraison f : allFrais) {
                if (f.contientVille(ville)) {
                    return f.calculerFrais(montantCommande);
                }
            }
            return BigDecimal.valueOf(10);
        }

        return frais.get().calculerFrais(montantCommande);
    }

    public BigDecimal calculerFraisExpress(String ville, BigDecimal montantCommande) {
        Optional<FraisLivraison> frais = fraisRepository.findByZoneIgnoreCase(ville);

        if (frais.isEmpty()) {
            List<FraisLivraison> allFrais = fraisRepository.findByActifTrue();
            for (FraisLivraison f : allFrais) {
                if (f.contientVille(ville)) {
                    return f.calculerFraisExpress(montantCommande);
                }
            }
            return null;
        }

        return frais.get().calculerFraisExpress(montantCommande);
    }

    public boolean estLivraisonGratuite(String ville, BigDecimal montantCommande) {
        Optional<FraisLivraison> frais = fraisRepository.findByZoneIgnoreCase(ville);

        if (frais.isEmpty()) {
            List<FraisLivraison> allFrais = fraisRepository.findByActifTrue();
            for (FraisLivraison f : allFrais) {
                if (f.contientVille(ville)) {
                    return f.estLivraisonGratuite(montantCommande);
                }
            }
            return false;
        }

        return frais.get().estLivraisonGratuite(montantCommande);
    }

    public BigDecimal montantRestantPourGratuit(String ville, BigDecimal montantCommande) {
        Optional<FraisLivraison> frais = fraisRepository.findByZoneIgnoreCase(ville);

        if (frais.isEmpty()) {
            List<FraisLivraison> allFrais = fraisRepository.findByActifTrue();
            for (FraisLivraison f : allFrais) {
                if (f.contientVille(ville)) {
                    return f.montantRestantPourGratuit(montantCommande);
                }
            }
            return null;
        }

        return frais.get().montantRestantPourGratuit(montantCommande);
    }

    public FraisLivraison updateFrais(String id, FraisLivraison frais) {
        Optional<FraisLivraison> existing = fraisRepository.findById(id);
        if (existing.isPresent()) {
            FraisLivraison f = existing.get();

            if (frais.getZone() != null) f.setZone(frais.getZone());
            if (frais.getVillesIncluses() != null) f.setVillesIncluses(frais.getVillesIncluses());
            if (frais.getFrais() != null) f.setFrais(frais.getFrais());
            if (frais.getMontantLivraisonGratuite() != null) f.setMontantLivraisonGratuite(frais.getMontantLivraisonGratuite());
            if (frais.getFraisExpress() != null) f.setFraisExpress(frais.getFraisExpress());
            if (frais.getDelaiJours() != null) f.setDelaiJours(frais.getDelaiJours());
            if (frais.getDelaiMinimum() != null) f.setDelaiMinimum(frais.getDelaiMinimum());
            if (frais.getDelaiMaximum() != null) f.setDelaiMaximum(frais.getDelaiMaximum());
            if (frais.getPriorite() != null) f.setPriorite(frais.getPriorite());

            f.setActif(frais.isActif());
            f.setExpressDisponible(frais.isExpressDisponible());
            f.setWeekendDisponible(frais.isWeekendDisponible());
            f.setDateModification(LocalDateTime.now());

            return fraisRepository.save(f);
        }
        throw new RuntimeException("Frais livraison non trouvés: " + id);
    }

    public FraisLivraison toggleActif(String id) {
        Optional<FraisLivraison> frais = fraisRepository.findById(id);
        if (frais.isPresent()) {
            FraisLivraison f = frais.get();
            f.setActif(!f.isActif());
            f.setDateModification(LocalDateTime.now());
            return fraisRepository.save(f);
        }
        throw new RuntimeException("Frais livraison non trouvés: " + id);
    }

    public void enregistrerLivraison(String zone) {
        Optional<FraisLivraison> frais = fraisRepository.findByZoneIgnoreCase(zone);
        if (frais.isPresent()) {
            FraisLivraison f = frais.get();
            f.enregistrerLivraison();
            fraisRepository.save(f);
        }
    }

    public void deleteFrais(String id) {
        fraisRepository.deleteById(id);
    }
}






