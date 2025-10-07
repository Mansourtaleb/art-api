package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.request.BanniereRequest;
import com.esprit.artdigital_backend.dto.response.BanniereResponse;
import com.esprit.artdigital_backend.model.Banniere;
import com.esprit.artdigital_backend.service.BanniereService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bannieres")
public class BanniereController {

    @Autowired
    private BanniereService banniereService;

    @GetMapping("/actives")
    public ResponseEntity<List<BanniereResponse>> getBannieresActives() {
        return ResponseEntity.ok(banniereService.getBannieresActives());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BanniereResponse>> getAllBannieres() {
        return ResponseEntity.ok(banniereService.getAllBannieres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BanniereResponse> getBanniereById(@PathVariable String id) {
        Banniere banniere = banniereService.getBanniereById(id);
        return ResponseEntity.ok(banniereService.convertToResponse(banniere));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BanniereResponse> creerBanniere(@Valid @RequestBody BanniereRequest request) {
        Banniere banniere = new Banniere(
                request.getTitre(),
                request.getImageUrl(),
                request.getTypeLien(),
                request.getLienVers(),
                request.getOrdre() != null ? request.getOrdre() : 0
        );
        banniere.setActif(request.getActif() != null ? request.getActif() : true);
        banniere.setDateDebut(request.getDateDebut());
        banniere.setDateFin(request.getDateFin());

        Banniere created = banniereService.creerBanniere(banniere);
        return new ResponseEntity<>(banniereService.convertToResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BanniereResponse> updateBanniere(
            @PathVariable String id,
            @Valid @RequestBody BanniereRequest request) {
        Banniere banniereDetails = new Banniere();
        banniereDetails.setTitre(request.getTitre());
        banniereDetails.setImageUrl(request.getImageUrl());
        banniereDetails.setTypeLien(request.getTypeLien());
        banniereDetails.setLienVers(request.getLienVers());
        banniereDetails.setOrdre(request.getOrdre());
        banniereDetails.setActif(request.getActif());
        banniereDetails.setDateDebut(request.getDateDebut());
        banniereDetails.setDateFin(request.getDateFin());

        Banniere updated = banniereService.updateBanniere(id, banniereDetails);
        return ResponseEntity.ok(banniereService.convertToResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBanniere(@PathVariable String id) {
        banniereService.deleteBanniere(id);
        return ResponseEntity.noContent().build();
    }
}