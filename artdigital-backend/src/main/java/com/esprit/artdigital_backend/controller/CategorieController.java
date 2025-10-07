package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.request.CategorieRequest;
import com.esprit.artdigital_backend.dto.response.CategorieResponse;
import com.esprit.artdigital_backend.model.Categorie;
import com.esprit.artdigital_backend.service.CategorieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    @GetMapping("/actives")
    public ResponseEntity<List<CategorieResponse>> getCategoriesActives() {
        return ResponseEntity.ok(categorieService.getAllCategoriesActives());
    }

    @GetMapping
    public ResponseEntity<Page<CategorieResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(categorieService.getAllCategories(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponse> getCategorieById(@PathVariable String id) {
        Categorie categorie = categorieService.getCategorieById(id);
        return ResponseEntity.ok(categorieService.convertToResponse(categorie));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategorieResponse> creerCategorie(@Valid @RequestBody CategorieRequest request) {
        Categorie categorie = new Categorie(request.getNom(), request.getDescription(), request.getImageUrl());
        if (request.getActif() != null) categorie.setActif(request.getActif());
        if (request.getOrdre() != null) categorie.setOrdre(request.getOrdre());

        Categorie created = categorieService.creerCategorie(categorie);
        return new ResponseEntity<>(categorieService.convertToResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategorieResponse> updateCategorie(
            @PathVariable String id,
            @Valid @RequestBody CategorieRequest request) {
        Categorie categorieDetails = new Categorie();
        categorieDetails.setNom(request.getNom());
        categorieDetails.setDescription(request.getDescription());
        categorieDetails.setImageUrl(request.getImageUrl());
        categorieDetails.setActif(request.getActif());
        categorieDetails.setOrdre(request.getOrdre());

        Categorie updated = categorieService.updateCategorie(id, categorieDetails);
        return ResponseEntity.ok(categorieService.convertToResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategorie(@PathVariable String id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}