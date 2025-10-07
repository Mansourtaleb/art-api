package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.response.BanniereResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.model.Banniere;
import com.esprit.artdigital_backend.repository.BanniereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BanniereService {

    @Autowired
    private BanniereRepository banniereRepository;

    public List<BanniereResponse> getBannieresActives() {
        LocalDateTime now = LocalDateTime.now();
        return banniereRepository.findByActifTrueOrderByOrdreAsc().stream()
                .filter(b -> {
                    if (b.getDateDebut() != null && b.getDateDebut().isAfter(now)) {
                        return false;
                    }
                    if (b.getDateFin() != null && b.getDateFin().isBefore(now)) {
                        return false;
                    }
                    return true;
                })
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<BanniereResponse> getAllBannieres() {
        return banniereRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Banniere getBanniereById(String id) {
        return banniereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bannière non trouvée avec l'ID: " + id));
    }

    public Banniere creerBanniere(Banniere banniere) {
        return banniereRepository.save(banniere);
    }

    public Banniere updateBanniere(String id, Banniere banniereDetails) {
        Banniere banniere = getBanniereById(id);

        if (banniereDetails.getTitre() != null) banniere.setTitre(banniereDetails.getTitre());
        if (banniereDetails.getImageUrl() != null) banniere.setImageUrl(banniereDetails.getImageUrl());
        if (banniereDetails.getTypeLien() != null) banniere.setTypeLien(banniereDetails.getTypeLien());
        if (banniereDetails.getLienVers() != null) banniere.setLienVers(banniereDetails.getLienVers());
        if (banniereDetails.getOrdre() != null) banniere.setOrdre(banniereDetails.getOrdre());
        if (banniereDetails.getActif() != null) banniere.setActif(banniereDetails.getActif());
        if (banniereDetails.getDateDebut() != null) banniere.setDateDebut(banniereDetails.getDateDebut());
        if (banniereDetails.getDateFin() != null) banniere.setDateFin(banniereDetails.getDateFin());

        return banniereRepository.save(banniere);
    }

    public void deleteBanniere(String id) {
        Banniere banniere = getBanniereById(id);
        banniereRepository.delete(banniere);
    }

    public BanniereResponse convertToResponse(Banniere banniere) {
        return new BanniereResponse(
                banniere.getId(),
                banniere.getTitre(),
                banniere.getImageUrl(),
                banniere.getTypeLien(),
                banniere.getLienVers(),
                banniere.getOrdre(),
                banniere.getActif(),
                banniere.getDateDebut(),
                banniere.getDateFin()
        );
    }
}