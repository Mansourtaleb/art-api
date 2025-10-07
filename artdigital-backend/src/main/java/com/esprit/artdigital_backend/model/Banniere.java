package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.enums.TypeLienBanniere;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "bannieres")
public class Banniere {
    @Id
    private String id;

    private String titre;
    private String imageUrl;
    private TypeLienBanniere typeLien;
    private String lienVers;
    private Integer ordre;
    private Boolean actif = true;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public Banniere(String titre, String imageUrl, TypeLienBanniere typeLien,
                    String lienVers, Integer ordre) {
        this.titre = titre;
        this.imageUrl = imageUrl;
        this.typeLien = typeLien;
        this.lienVers = lienVers;
        this.ordre = ordre;
        this.actif = true;
    }
}