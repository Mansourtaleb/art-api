package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.enums.StatutProduitPersonnalise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitPersonnaliseResponse {
    private String id;
    private String clientId;
    private String clientNom;
    private String typeProduit;
    private String templateId;
    private Map<String, String> personnalisations;
    private String logoUrl;
    private BigDecimal prix;
    private StatutProduitPersonnalise statut;
    private String previewUrl;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String notes;
}