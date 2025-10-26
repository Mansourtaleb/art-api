package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.response.UtilisateurResponse;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Récupérer mon profil
    @GetMapping("/me")
    public ResponseEntity<UtilisateurResponse> getMonProfil(Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
        return ResponseEntity.ok(utilisateurService.convertToResponse(utilisateur));
    }

    // Mettre à jour mon profil
    @PutMapping("/me")
    public ResponseEntity<UtilisateurResponse> updateMonProfil(
            @RequestBody Utilisateur utilisateurDetails,
            Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur utilisateur = utilisateurService.updateUtilisateur(userId, utilisateurDetails);
        return ResponseEntity.ok(utilisateurService.convertToResponse(utilisateur));
    }

    // Changer mon mot de passe
    @PatchMapping("/me/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        String userId = authentication.getName();
        String ancienMotDePasse = request.get("ancienMotDePasse");
        String nouveauMotDePasse = request.get("nouveauMotDePasse");

        utilisateurService.changePassword(userId, ancienMotDePasse, nouveauMotDePasse);

        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    // Ajouter une adresse
    @PostMapping("/me/adresses")
    public ResponseEntity<UtilisateurResponse> ajouterAdresse(
            @RequestBody AdresseLivraison adresse,
            Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
        utilisateur.ajouterAdresse(adresse);
        Utilisateur updated = utilisateurService.updateUtilisateur(userId, utilisateur);
        return ResponseEntity.ok(utilisateurService.convertToResponse(updated));
    }

    // Supprimer une adresse
    @DeleteMapping("/me/adresses/{index}")
    public ResponseEntity<UtilisateurResponse> supprimerAdresse(
            @PathVariable int index,
            Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
        utilisateur.supprimerAdresse(index);
        Utilisateur updated = utilisateurService.updateUtilisateur(userId, utilisateur);
        return ResponseEntity.ok(utilisateurService.convertToResponse(updated));
    }

    // Admin: Liste tous les utilisateurs
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUtilisateurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs(page, size));
    }

    // Admin: Supprimer un utilisateur
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable String id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}