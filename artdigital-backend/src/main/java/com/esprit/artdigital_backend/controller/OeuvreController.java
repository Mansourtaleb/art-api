package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.filter.OeuvreFilterDTO;
import com.esprit.artdigital_backend.dto.request.OeuvreRequest;
import com.esprit.artdigital_backend.dto.response.OeuvreResponse;
import com.esprit.artdigital_backend.model.Oeuvre;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.service.OeuvreService;
import com.esprit.artdigital_backend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/oeuvres")
public class OeuvreController {

    @Autowired
    private OeuvreService oeuvreService;

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<Page<OeuvreResponse>> getAllOeuvres(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) BigDecimal prixMin,
            @RequestParam(required = false) BigDecimal prixMax,
            @RequestParam(required = false) String artisteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        OeuvreFilterDTO filter = new OeuvreFilterDTO(categorie, prixMin, prixMax, artisteId, page, size);
        Page<OeuvreResponse> oeuvres = oeuvreService.getAllOeuvres(filter);
        return ResponseEntity.ok(oeuvres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OeuvreResponse> getOeuvreById(@PathVariable String id) {
        Oeuvre oeuvre = oeuvreService.getOeuvreById(id);
        OeuvreResponse response = oeuvreService.convertToResponse(oeuvre);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artiste/{artisteId}")
    public ResponseEntity<Page<OeuvreResponse>> getOeuvresByArtiste(
            @PathVariable String artisteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OeuvreResponse> oeuvres = oeuvreService.getOeuvresByArtiste(artisteId, page, size);
        return ResponseEntity.ok(oeuvres);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ARTISTE', 'ADMIN')")
    public ResponseEntity<OeuvreResponse> creerOeuvre(
            @Valid @RequestBody OeuvreRequest request,
            Authentication authentication) {
        String userId = authentication.getName();

        Oeuvre oeuvre = new Oeuvre(
                request.getTitre(),
                request.getDescription(),
                request.getCategorie(),
                request.getPrix(),
                request.getQuantiteDisponible(),
                userId,
                null
        );

        if (request.getImages() != null) {
            oeuvre.setImages(request.getImages());
        }
        if (request.getStatut() != null) {
            oeuvre.setStatut(request.getStatut());
        }

        Oeuvre nouvelleOeuvre = oeuvreService.creerOeuvre(oeuvre, userId);
        OeuvreResponse response = oeuvreService.convertToResponse(nouvelleOeuvre);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ARTISTE', 'ADMIN')")
    public ResponseEntity<OeuvreResponse> updateOeuvre(
            @PathVariable String id,
            @Valid @RequestBody OeuvreRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        RoleUtilisateur role = RoleUtilisateur.valueOf(userRole.replace("ROLE_", ""));

        Oeuvre oeuvreDetails = new Oeuvre();
        oeuvreDetails.setTitre(request.getTitre());
        oeuvreDetails.setDescription(request.getDescription());
        oeuvreDetails.setCategorie(request.getCategorie());
        oeuvreDetails.setPrix(request.getPrix());
        oeuvreDetails.setQuantiteDisponible(request.getQuantiteDisponible());
        oeuvreDetails.setImages(request.getImages());
        oeuvreDetails.setStatut(request.getStatut());

        Oeuvre oeuvre = oeuvreService.updateOeuvre(id, oeuvreDetails, userId, role);
        OeuvreResponse response = oeuvreService.convertToResponse(oeuvre);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ARTISTE', 'ADMIN')")
    public ResponseEntity<Void> deleteOeuvre(
            @PathVariable String id,
            Authentication authentication) {
        String userId = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        RoleUtilisateur role = RoleUtilisateur.valueOf(userRole.replace("ROLE_", ""));

        oeuvreService.deleteOeuvre(id, userId, role);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/avis")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OeuvreResponse> ajouterAvis(
            @PathVariable String id,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userId = authentication.getName();
        Utilisateur client = utilisateurService.getUtilisateurById(userId);

        Integer note = (Integer) request.get("note");
        String commentaire = (String) request.get("commentaire");

        Oeuvre oeuvre = oeuvreService.ajouterAvis(id, userId, client.getNom(), note, commentaire);
        OeuvreResponse response = oeuvreService.convertToResponse(oeuvre);
        return ResponseEntity.ok(response);
    }
}