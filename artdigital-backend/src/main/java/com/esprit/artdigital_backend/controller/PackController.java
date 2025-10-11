package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.model.Pack;
import com.esprit.artdigital_backend.model.enums.Occasion;
import com.esprit.artdigital_backend.service.PackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/packs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PackController {

    private final PackService packService;

    @PostMapping
    public ResponseEntity<Pack> creerPack(@RequestBody Pack pack) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packService.creerPack(pack));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pack> getPackById(@PathVariable String id) {
        return packService.getPackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pack>> getAllPacks() {
        return ResponseEntity.ok(packService.getAllPacks());
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Pack>> getPacksActifs() {
        return ResponseEntity.ok(packService.getPacksActifs());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Pack>> getPacksFeatured() {
        return ResponseEntity.ok(packService.getPacksFeatured());
    }

    @GetMapping("/occasion/{occasion}")
    public ResponseEntity<List<Pack>> getPacksByOccasion(@PathVariable Occasion occasion) {
        return ResponseEntity.ok(packService.getPacksByOccasion(occasion));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Pack>> getPacksDisponibles() {
        return ResponseEntity.ok(packService.getPacksDisponibles());
    }

    @GetMapping("/en-stock")
    public ResponseEntity<List<Pack>> getPacksEnStock() {
        return ResponseEntity.ok(packService.getPacksEnStock());
    }

    @GetMapping("/populaires")
    public ResponseEntity<List<Pack>> getPacksPopulaires() {
        return ResponseEntity.ok(packService.getPacksPopulaires());
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Pack>> rechercherPacks(@RequestParam String nom) {
        return ResponseEntity.ok(packService.rechercherPacks(nom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pack> updatePack(@PathVariable String id, @RequestBody Pack pack) {
        try {
            return ResponseEntity.ok(packService.updatePack(id, pack));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/toggle-actif")
    public ResponseEntity<Pack> toggleActif(@PathVariable String id) {
        try {
            return ResponseEntity.ok(packService.toggleActif(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/vendre")
    public ResponseEntity<Map<String, Object>> vendrePack(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        Integer quantite = body.get("quantite");
        boolean success = packService.vendrePack(id, quantite);
        return ResponseEntity.ok(Map.of("success", success));
    }

    @PostMapping("/{id}/incrementer-vues")
    public ResponseEntity<Void> incrementerVues(@PathVariable String id) {
        packService.incrementerVues(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/incrementer-ajouts-panier")
    public ResponseEntity<Void> incrementerAjoutsPanier(@PathVariable String id) {
        packService.incrementerAjoutsPanier(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable String id) {
        packService.deletePack(id);
        return ResponseEntity.noContent().build();
    }
}






