package com.esprit.artdigital_backend.dto.filter;

import com.esprit.artdigital_backend.model.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFilterDTO {
    private String clientId;
    private StatutCommande statut;
    private int page;
    private int size;
}