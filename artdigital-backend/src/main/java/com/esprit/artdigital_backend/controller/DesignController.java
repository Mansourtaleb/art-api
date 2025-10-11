package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.model.DesignPredifini;
import com.esprit.artdigital_backend.model.enums.CategorieDesign;
import com.esprit.artdigital_backend.model.enums.TypeProduit;
import com.esprit.artdigital_backend.service.DesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DesignController {

    private final DesignService designService;

    @PostMapping
    public ResponseEntity<DesignPredifini> creerDesign(@RequestBody DesignPredifini design) {
        return ResponseEntity.status(HttpStatus.CREATED).body(designService.creerDesign(design));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignPredifini> getDesignById(@PathVariable String id) {
        return designService.getDesignById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DesignPredifini>> getAllDesigns() {
        return ResponseEntity.ok(designService.getAllDesigns());
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<DesignPredifini>> getDesignsActifs() {
        return ResponseEntity.ok(designService.getDesignsActifs());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<DesignPredifini>> getDesignsFeatured() {
        return ResponseEntity.ok(designService.getDesignsFeatured());
    }

    @GetMapping("/gratuits")
    public ResponseEntity<List<DesignPredifini>> getDesignsGratuits() {
        return ResponseEntity.ok(designService.getDesignsGratuits());
    }

    @GetMapping("/premium")
    public ResponseEntity<List<DesignPredifini>> getDesignsPremium() {
        return ResponseEntity.ok(designService.getDesignsPremium());
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<DesignPredifini>> getDesignsByCategorie(@PathVariable CategorieDesign categorie) {
        return ResponseEntity.ok(designService.getDesignsByCategorie(categorie));
    }

    @GetMapping("/compatibles/{typeProduit}")
    public ResponseEntity<List<DesignPredifini>> getDesignsCompatibles(@PathVariable TypeProduit typeProduit) {
        return ResponseEntity.ok(designService.getDesignsCompatibles(typeProduit));
    }

    @GetMapping("/populaires")
    public ResponseEntity<List<DesignPredifini>> getDesignsPopulaires() {
        return ResponseEntity.ok(designService.getDesignsPopulaires());
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<DesignPredifini>> getDesignsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(designService.getDesignsByTag(tag));
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<DesignPredifini>> rechercherDesigns(@RequestParam String nom) {
        return ResponseEntity.ok(designService.rechercherDesigns(nom));
    }

    @GetMapping("/recents")
    public ResponseEntity<List<DesignPredifini>> getDesignsRecents() {
        return ResponseEntity.ok(designService.getDesignsRecents());
    }

    @GetMapping("/ordonnes")
    public ResponseEntity<List<DesignPredifini>> getDesignsOrdonnes() {
        return ResponseEntity.ok(designService.getDesignsOrdonnes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesignPredifini> updateDesign(@PathVariable String id, @RequestBody DesignPredifini design) {
        try {
            return ResponseEntity.ok(designService.updateDesign(id, design));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/incrementer-utilisations")
    public ResponseEntity<Void> incrementerUtilisations(@PathVariable String id) {
        designService.incrementerUtilisations(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/incrementer-vues")
    public ResponseEntity<Void> incrementerVues(@PathVariable String id) {
        designService.incrementerVues(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/incrementer-favoris")
    public ResponseEntity<Void> incrementerFavoris(@PathVariable String id) {
        designService.incrementerFavoris(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesign(@PathVariable String id) {
        designService.deleteDesign(id);
        return ResponseEntity.noContent().build();
    }
}
