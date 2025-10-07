package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitCommande {
    private String oeuvreId;
    private String titre;
    private BigDecimal prix;
    private Integer quantite;
    private String imageUrl;
}