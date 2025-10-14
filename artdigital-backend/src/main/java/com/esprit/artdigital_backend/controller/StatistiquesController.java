package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.dto.response.StatistiquesDTO;
import com.esprit.artdigital_backend.service.StatistiquesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiques")
@CrossOrigin(origins = "*")
public class StatistiquesController {

    @Autowired
    private StatistiquesService statistiquesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiquesDTO> getStatistiques() {
        return ResponseEntity.ok(statistiquesService.getStatistiquesGlobales());
    }
}