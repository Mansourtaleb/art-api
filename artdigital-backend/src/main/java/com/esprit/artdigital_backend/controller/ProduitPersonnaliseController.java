package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.request.ProduitPersonnaliseRequest;
import com.esprit.artdigital_backend.dto.response.ProduitPersonnaliseResponse;
import com.esprit.artdigital_backend.model.ProduitPersonnalise;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutProduitPersonnalise;
import com.esprit.artdigital_backend.service.ProduitPersonnaliseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/produits-personnalises")
public class ProduitPersonnaliseController {

    @Autowired
    private ProduitPersonnaliseService produitPersonnaliseService;

    @GetMapping
    public ResponseEntity<Page<ProduitPersonnaliseResponse>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        Page<ProduitPersonnaliseResponse> produits =
                produitPersonnaliseService.getAllProduits(page, size, userId, role);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitPersonnaliseResponse> getProduitById(
            @PathVariable String id,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        ProduitPersonnalise produit = produitPersonnaliseService.getProduitById(id, userId, role);
        return ResponseEntity.ok(produitPersonnaliseService.convertToResponse(produit));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProduitPersonnaliseResponse> creerProduit(
            @Valid @RequestBody ProduitPersonnaliseRequest request,
            Authentication authentication) {

        String userId = authentication.getName();
        ProduitPersonnalise produit = produitPersonnaliseService.creerProduitPersonnalise(request, userId);
        return new ResponseEntity<>(
                produitPersonnaliseService.convertToResponse(produit),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTISTE')")
    public ResponseEntity<ProduitPersonnaliseResponse> updateStatut(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        StatutProduitPersonnalise nouveauStatut = StatutProduitPersonnalise.valueOf(request.get("statut"));
        ProduitPersonnalise produit = produitPersonnaliseService.updateStatut(id, nouveauStatut, userId, role);
        return ResponseEntity.ok(produitPersonnaliseService.convertToResponse(produit));
    }

    @PutMapping("/{id}/preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTISTE')")
    public ResponseEntity<ProduitPersonnaliseResponse> updatePreview(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        String previewUrl = request.get("previewUrl");
        ProduitPersonnalise produit = produitPersonnaliseService.updatePreview(id, previewUrl, role);
        return ResponseEntity.ok(produitPersonnaliseService.convertToResponse(produit));
    }
}