package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.response.PanierResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.model.Oeuvre;
import com.esprit.artdigital_backend.model.Panier;
import com.esprit.artdigital_backend.model.embedded.PanierItem;
import com.esprit.artdigital_backend.repository.PanierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanierService {

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private OeuvreService oeuvreService;

    public Panier getOrCreatePanier(String utilisateurId) {
        return panierRepository.findByUtilisateurId(utilisateurId)
                .orElseGet(() -> {
                    Panier nouveauPanier = new Panier(utilisateurId);
                    return panierRepository.save(nouveauPanier);
                });
    }

    public Panier getPanierByUserId(String utilisateurId) {
        return panierRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Panier non trouvé pour cet utilisateur"));
    }

    @Transactional
    public Panier ajouterAuPanier(String utilisateurId, String oeuvreId, Integer quantite,
                                  String notePersonnalisation, String imagePersonnalisationUrl) {
        // Récupérer ou créer panier
        Panier panier = getOrCreatePanier(utilisateurId);

        // Vérifier que l'oeuvre existe et a du stock
        Oeuvre oeuvre = oeuvreService.getOeuvreById(oeuvreId);

        if (oeuvre.getQuantiteDisponible() < quantite) {
            throw new IllegalArgumentException(
                    "Stock insuffisant. Disponible: " + oeuvre.getQuantiteDisponible()
            );
        }

        // Créer item panier
        PanierItem item = new PanierItem(
                oeuvre.getId(),
                oeuvre.getTitre(),
                oeuvre.getPrix(),
                quantite,
                oeuvre.getImages().isEmpty() ? null : oeuvre.getImages().get(0),
                oeuvre.getQuantiteDisponible(),
                notePersonnalisation,
                imagePersonnalisationUrl
        );

        // Ajouter au panier
        panier.ajouterItem(item);

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier updateQuantite(String utilisateurId, String oeuvreId, Integer nouvelleQuantite) {
        Panier panier = getPanierByUserId(utilisateurId);

        // Vérifier stock disponible
        Oeuvre oeuvre = oeuvreService.getOeuvreById(oeuvreId);
        if (nouvelleQuantite > oeuvre.getQuantiteDisponible()) {
            throw new IllegalArgumentException(
                    "Stock insuffisant. Disponible: " + oeuvre.getQuantiteDisponible()
            );
        }

        panier.updateQuantite(oeuvreId, nouvelleQuantite);

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier retirerDuPanier(String utilisateurId, String oeuvreId) {
        Panier panier = getPanierByUserId(utilisateurId);
        panier.retirerItem(oeuvreId);
        return panierRepository.save(panier);
    }

    @Transactional
    public void viderPanier(String utilisateurId) {
        Panier panier = getPanierByUserId(utilisateurId);
        panier.vider();
        panierRepository.save(panier);
    }

    public PanierResponse convertToResponse(Panier panier) {
        return new PanierResponse(
                panier.getId(),
                panier.getUtilisateurId(),
                panier.getItems(),
                panier.getSousTotal(),
                panier.getNombreItems(),
                panier.getDateModification()
        );
    }
}