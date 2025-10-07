package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.response.CategorieResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.model.Categorie;
import com.esprit.artdigital_backend.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    public List<CategorieResponse> getAllCategoriesActives() {
        return categorieRepository.findByActifTrueOrderByOrdreAsc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<CategorieResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "ordre"));
        return categorieRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Categorie getCategorieById(String id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + id));
    }

    public Categorie creerCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorie(String id, Categorie categorieDetails) {
        Categorie categorie = getCategorieById(id);

        if (categorieDetails.getNom() != null) categorie.setNom(categorieDetails.getNom());
        if (categorieDetails.getDescription() != null) categorie.setDescription(categorieDetails.getDescription());
        if (categorieDetails.getImageUrl() != null) categorie.setImageUrl(categorieDetails.getImageUrl());
        if (categorieDetails.getActif() != null) categorie.setActif(categorieDetails.getActif());
        if (categorieDetails.getOrdre() != null) categorie.setOrdre(categorieDetails.getOrdre());

        return categorieRepository.save(categorie);
    }

    public void deleteCategorie(String id) {
        Categorie categorie = getCategorieById(id);
        categorieRepository.delete(categorie);
    }

    public CategorieResponse convertToResponse(Categorie categorie) {
        return new CategorieResponse(
                categorie.getId(),
                categorie.getNom(),
                categorie.getDescription(),
                categorie.getImageUrl(),
                categorie.getActif(),
                categorie.getOrdre()
        );
    }
}