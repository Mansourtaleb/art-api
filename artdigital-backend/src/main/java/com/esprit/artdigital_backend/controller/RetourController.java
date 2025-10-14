package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.model.Retour;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutRetour;
import com.esprit.artdigital_backend.service.RetourService;
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
@RequestMapping("/api/retours")
@CrossOrigin(origins = "*")
public class RetourController {

    @Autowired
    private RetourService retourService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Retour> creerRetour(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String userId = authentication.getName();
        String commandeId = request.get("commandeId");
        String motif = request.get("motif");
        String description = request.get("description");
        String produitsConcernes = request.get("produitsConcernes");

        Retour retour = retourService.creerRetour(commandeId, motif, description,
                produitsConcernes, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(retour);
    }

    @GetMapping("/mes-retours")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Retour>> getMesRetours(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(retourService.getMesRetours(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Retour>> getAllRetours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(retourService.getAllRetours(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Retour> getRetourById(
            @PathVariable String id,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        Retour retour = retourService.getRetourById(id, userId, role);
        return ResponseEntity.ok(retour);
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Retour>> getRetoursByStatut(@PathVariable StatutRetour statut) {
        return ResponseEntity.ok(retourService.getRetoursByStatut(statut));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Retour> changerStatut(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        StatutRetour statut = StatutRetour.valueOf(request.get("statut"));
        String commentaire = request.get("commentaire");

        Retour retour = retourService.changerStatut(id, statut, commentaire);
        return ResponseEntity.ok(retour);
    }

    @PatchMapping("/{id}/accepter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Retour> accepterRetour(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> request) {

        String commentaire = request != null ? request.get("commentaire") : null;
        Retour retour = retourService.accepterRetour(id, commentaire);
        return ResponseEntity.ok(retour);
    }

    @PatchMapping("/{id}/refuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Retour> refuserRetour(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        String commentaire = request.get("commentaire");
        Retour retour = retourService.refuserRetour(id, commentaire);
        return ResponseEntity.ok(retour);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRetour(@PathVariable String id) {
        retourService.deleteRetour(id);
        return ResponseEntity.noContent().build();
    }
}