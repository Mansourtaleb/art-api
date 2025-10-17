package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.filter.CommandeFilterDTO;
import com.esprit.artdigital_backend.dto.request.CommandeRequest;
import com.esprit.artdigital_backend.dto.response.CommandeResponse;
import com.esprit.artdigital_backend.model.Commande;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
import com.esprit.artdigital_backend.service.CommandeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    // NOUVEAU - Mes commandes (Client)
    @GetMapping("/mes-commandes")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CommandeResponse>> getMesCommandes(Authentication authentication) {
        String userId = authentication.getName();

        // Utiliser le filtre avec clientId = userId
        CommandeFilterDTO filter = new CommandeFilterDTO(userId, null, 0, 100);
        Page<CommandeResponse> commandes = commandeService.getAllCommandes(
                filter,
                userId,
                RoleUtilisateur.CLIENT
        );

        return ResponseEntity.ok(commandes.getContent());
    }

    // Liste toutes les commandes (Admin) ou filtrées
    @GetMapping
    public ResponseEntity<Page<CommandeResponse>> getAllCommandes(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) StatutCommande statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        CommandeFilterDTO filter = new CommandeFilterDTO(clientId, statut, page, size);
        Page<CommandeResponse> commandes = commandeService.getAllCommandes(filter, userId, role);
        return ResponseEntity.ok(commandes);
    }

    // Détails d'une commande
    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getCommandeById(
            @PathVariable String id,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        Commande commande = commandeService.getCommandeById(id, userId, role);
        return ResponseEntity.ok(commandeService.convertToResponse(commande));
    }

    // Créer une commande
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CommandeResponse> creerCommande(
            @Valid @RequestBody CommandeRequest request,
            Authentication authentication) {

        String userId = authentication.getName();
        Commande commande = commandeService.creerCommande(request, userId);
        return new ResponseEntity<>(commandeService.convertToResponse(commande), HttpStatus.CREATED);
    }

    // CORRIGÉ - Changer statut (PATCH au lieu de PUT)
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTISTE')")
    public ResponseEntity<CommandeResponse> updateStatutCommande(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String userId = authentication.getName();
        RoleUtilisateur role = RoleUtilisateur.valueOf(
                authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );

        StatutCommande nouveauStatut = StatutCommande.valueOf(request.get("statut"));
        Commande commande = commandeService.updateStatutCommande(id, nouveauStatut, userId, role);
        return ResponseEntity.ok(commandeService.convertToResponse(commande));
    }

    // NOUVEAU - Annuler une commande (Client)
    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CommandeResponse> annulerCommande(
            @PathVariable String id,
            Authentication authentication) {

        String userId = authentication.getName();
        Commande commande = commandeService.annulerCommande(id, userId);
        return ResponseEntity.ok(commandeService.convertToResponse(commande));
    }
}