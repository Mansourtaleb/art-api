package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.enums.TypeLienBanniere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanniereResponse {
    private String id;
    private String titre;
    private String imageUrl;
    private TypeLienBanniere typeLien;
    private String lienVers;
    private Integer ordre;
    private Boolean actif;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}