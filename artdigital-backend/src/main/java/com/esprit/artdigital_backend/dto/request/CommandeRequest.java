package com.esprit.artdigital_backend.dto.request;

import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequest {

    @NotEmpty(message = "La liste des produits ne peut pas être vide")
    private List<ProduitCommandeRequest> produits;

    @NotNull(message = "L'adresse de livraison est requise")
    private AdresseLivraison adresseLivraison;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitCommandeRequest {
        @NotNull(message = "L'ID de l'oeuvre est requis")
        private String oeuvreId;

        @NotNull(message = "La quantité est requise")
        private Integer quantite;
    }
}