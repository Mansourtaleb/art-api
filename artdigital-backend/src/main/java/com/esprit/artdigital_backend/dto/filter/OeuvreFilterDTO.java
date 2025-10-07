package com.esprit.artdigital_backend.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OeuvreFilterDTO {
    private String categorie;
    private BigDecimal prixMin;
    private BigDecimal prixMax;
    private String artisteId;
    private int page;
    private int size;
}