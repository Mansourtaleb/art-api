package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvisOeuvre {
    private String clientId;
    private String clientNom;
    private Integer note;
    private String commentaire;
    private LocalDateTime dateAvis;

    public AvisOeuvre(String clientId, String clientNom, Integer note, String commentaire) {
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = LocalDateTime.now();
    }
}