package com.esprit.artdigital_backend.dto.response;

import com.esprit.artdigital_backend.model.embedded.PanierItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierResponse {
    private String id;
    private String utilisateurId;
    private List<PanierItem> items;
    private BigDecimal sousTotal;
    private Integer nombreItems;
    private LocalDateTime dateModification;
}