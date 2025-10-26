package com.esprit.artdigital_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PanierItemRequest {
    @NotBlank(message = "L'ID de l'oeuvre est obligatoire")
    private String oeuvreId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    // Optionnel pour personnalisation
    private String notePersonnalisation;
    private String imagePersonnalisationUrl;
}