package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.embedded.AvisOeuvre;
import com.esprit.artdigital_backend.model.enums.StatutOeuvre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OeuvreResponse {
    private String id;
    private String titre;
    private String description;
    private String categorie;
    private BigDecimal prix;
    private Integer quantiteDisponible;
    private String artisteId;
    private String artisteNom;
    private List<String> images;
    private StatutOeuvre statut;
    private LocalDateTime dateCreation;
    private List<AvisOeuvre> avis;
    private Double notemoyenne;
}