package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.response.UtilisateurResponse;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.repository.UtilisateurRepository;
import com.esprit.artdigital_backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UtilisateurResponse>> getAllUtilisateurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs(page, size));
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurResponse> getUtilisateurConnecte(Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
        return ResponseEntity.ok(utilisateurService.convertToResponse(utilisateur));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponse> getUtilisateurById(
            @PathVariable String id,
            Authentication authentication) {
        String userId = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();

        if (!userRole.equals("ROLE_ADMIN") && !userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateurService.convertToResponse(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponse> updateUtilisateur(
            @PathVariable String id,
            @RequestBody Utilisateur utilisateurDetails,
            Authentication authentication) {
        String userId = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();

        if (!userRole.equals("ROLE_ADMIN") && !userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur updated = utilisateurService.updateUtilisateur(id, utilisateurDetails);
        return ResponseEntity.ok(utilisateurService.convertToResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable String id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        if (!authentication.getName().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String ancienMotDePasse = request.get("ancienMotDePasse");
        String nouveauMotDePasse = request.get("nouveauMotDePasse");

        utilisateurService.changePassword(id, ancienMotDePasse, nouveauMotDePasse);

        return ResponseEntity.ok(Map.of(
                "message", "Mot de passe modifié avec succès"
        ));
    }

    @GetMapping("/{id}/adresses")
    public ResponseEntity<List<AdresseLivraison>> getAdresses(
            @PathVariable String id,
            Authentication authentication) {
        if (!authentication.getName().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur.getAdresses());
    }

    @PostMapping("/{id}/adresses")
    public ResponseEntity<List<AdresseLivraison>> ajouterAdresse(
            @PathVariable String id,
            @RequestBody AdresseLivraison adresse,
            Authentication authentication) {
        if (!authentication.getName().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        utilisateur.ajouterAdresse(adresse);

        // ✅ CORRECTION : Sauvegarder directement sans passer par updateUtilisateur
        Utilisateur saved = utilisateurRepository.save(utilisateur);

        return ResponseEntity.ok(saved.getAdresses());
    }

    @DeleteMapping("/{id}/adresses/{index}")
    public ResponseEntity<List<AdresseLivraison>> supprimerAdresse(
            @PathVariable String id,
            @PathVariable int index,
            Authentication authentication) {
        if (!authentication.getName().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        utilisateur.supprimerAdresse(index);
        utilisateurService.updateUtilisateur(id, utilisateur);

        return ResponseEntity.ok(utilisateur.getAdresses());
    }
}