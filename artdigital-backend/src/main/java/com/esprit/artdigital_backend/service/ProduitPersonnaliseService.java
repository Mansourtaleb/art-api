package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.request.ProduitPersonnaliseRequest;
import com.esprit.artdigital_backend.dto.response.ProduitPersonnaliseResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.exception.UnauthorizedException;
import com.esprit.artdigital_backend.model.ProduitPersonnalise;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutProduitPersonnalise;
import com.esprit.artdigital_backend.repository.ProduitPersonnaliseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProduitPersonnaliseService {

    @Autowired
    private ProduitPersonnaliseRepository produitPersonnaliseRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public ProduitPersonnalise creerProduitPersonnalise(ProduitPersonnaliseRequest request, String userId) {
        Utilisateur client = utilisateurService.getUtilisateurById(userId);

        ProduitPersonnalise produit = new ProduitPersonnalise(
                userId,
                client.getNom(),
                request.getTypeProduit(),
                request.getTemplateId(),
                request.getPersonnalisations(),
                request.getPrix()
        );

        produit.setLogoUrl(request.getLogoUrl());
        produit.setNotes(request.getNotes());

        return produitPersonnaliseRepository.save(produit);
    }

    public ProduitPersonnalise getProduitById(String id, String userId, RoleUtilisateur userRole) {
        ProduitPersonnalise produit = produitPersonnaliseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit personnalisé non trouvé avec l'ID: " + id));

        // Vérifier autorisation pour client
        if (userRole == RoleUtilisateur.CLIENT && !produit.getClientId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à voir ce produit");
        }

        return produit;
    }

    public Page<ProduitPersonnaliseResponse> getAllProduits(int page, int size, String userId, RoleUtilisateur userRole) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
        Page<ProduitPersonnalise> produits;

        if (userRole == RoleUtilisateur.CLIENT) {
            // Client ne voit que ses produits
            produits = produitPersonnaliseRepository.findByClientId(userId, pageable);
        } else {
            // Admin/Artiste voient tout
            produits = produitPersonnaliseRepository.findAll(pageable);
        }

        return produits.map(this::convertToResponse);
    }

    public ProduitPersonnalise updateStatut(String id, StatutProduitPersonnalise nouveauStatut,
                                            String userId, RoleUtilisateur userRole) {
        if (userRole == RoleUtilisateur.CLIENT) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier le statut");
        }

        ProduitPersonnalise produit = produitPersonnaliseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit personnalisé non trouvé"));

        produit.updateStatut(nouveauStatut);
        return produitPersonnaliseRepository.save(produit);
    }

    public ProduitPersonnalise updatePreview(String id, String previewUrl, RoleUtilisateur userRole) {
        if (userRole == RoleUtilisateur.CLIENT) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier l'aperçu");
        }

        ProduitPersonnalise produit = produitPersonnaliseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit personnalisé non trouvé"));

        produit.setPreviewUrl(previewUrl);
        return produitPersonnaliseRepository.save(produit);
    }

    public ProduitPersonnaliseResponse convertToResponse(ProduitPersonnalise produit) {
        return new ProduitPersonnaliseResponse(
                produit.getId(),
                produit.getClientId(),
                produit.getClientNom(),
                produit.getTypeProduit(),
                produit.getTemplateId(),
                produit.getPersonnalisations(),
                produit.getLogoUrl(),
                produit.getPrix(),
                produit.getStatut(),
                produit.getPreviewUrl(),
                produit.getDateCreation(),
                produit.getDateModification(),
                produit.getNotes()
        );
    }
}