package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.request.PanierItemRequest;
import com.esprit.artdigital_backend.dto.response.PanierResponse;
import com.esprit.artdigital_backend.model.Panier;
import com.esprit.artdigital_backend.service.PanierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/panier")
@PreAuthorize("hasRole('CLIENT')")
public class PanierController {

    @Autowired
    private PanierService panierService;

    // Récupérer mon panier
    @GetMapping
    public ResponseEntity<PanierResponse> getMonPanier(Authentication authentication) {
        String userId = authentication.getName();
        Panier panier = panierService.getOrCreatePanier(userId);
        return ResponseEntity.ok(panierService.convertToResponse(panier));
    }

    // Ajouter un produit
    @PostMapping("/items")
    public ResponseEntity<PanierResponse> ajouterAuPanier(
            @Valid @RequestBody PanierItemRequest request,
            Authentication authentication) {
        String userId = authentication.getName();

        Panier panier = panierService.ajouterAuPanier(
                userId,
                request.getOeuvreId(),
                request.getQuantite(),
                request.getNotePersonnalisation(),
                request.getImagePersonnalisationUrl()
        );

        return ResponseEntity.ok(panierService.convertToResponse(panier));
    }

    // Modifier quantité
    @PatchMapping("/items/{oeuvreId}")
    public ResponseEntity<PanierResponse> updateQuantite(
            @PathVariable String oeuvreId,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        String userId = authentication.getName();
        Integer nouvelleQuantite = request.get("quantite");

        Panier panier = panierService.updateQuantite(userId, oeuvreId, nouvelleQuantite);
        return ResponseEntity.ok(panierService.convertToResponse(panier));
    }

    // Retirer un produit
    @DeleteMapping("/items/{oeuvreId}")
    public ResponseEntity<PanierResponse> retirerDuPanier(
            @PathVariable String oeuvreId,
            Authentication authentication) {
        String userId = authentication.getName();
        Panier panier = panierService.retirerDuPanier(userId, oeuvreId);
        return ResponseEntity.ok(panierService.convertToResponse(panier));
    }

    // Vider le panier
    @DeleteMapping
    public ResponseEntity<Void> viderPanier(Authentication authentication) {
        String userId = authentication.getName();
        panierService.viderPanier(userId);
        return ResponseEntity.noContent().build();
    }
}