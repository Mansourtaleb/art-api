package com.esprit.artdigital_backend.dto.request;

import com.esprit.artdigital_backend.model.enums.TypeLienBanniere;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanniereRequest {
    @NotBlank(message = "Le titre est requis")
    private String titre;

    @NotBlank(message = "L'image est requise")
    private String imageUrl;

    @NotNull(message = "Le type de lien est requis")
    private TypeLienBanniere typeLien;

    @NotBlank(message = "Le lien est requis")
    private String lienVers;

    private Integer ordre;
    private Boolean actif;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}