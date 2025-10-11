package com.esprit.artdigital_backend.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant les détails de personnalisation d'un produit par le client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnalisationClient {

    private String imageUploadUrl;
    private String designPredefiniId;
    private String nomDesign;
    private String textePersonnalise;
    private String policeTexte;
    private String couleurTexte;
    private String positionTexte;
    private String zone;
    private Map<String, String> optionsSupplementaires;
    private String urlPrevisualisation;

    public PersonnalisationClient(String imageUploadUrl) {
        this.imageUploadUrl = imageUploadUrl;
        this.optionsSupplementaires = new HashMap<>();
    }

    public PersonnalisationClient(String designPredefiniId, String nomDesign) {
        this.designPredefiniId = designPredefiniId;
        this.nomDesign = nomDesign;
        this.optionsSupplementaires = new HashMap<>();
    }

    public boolean aPersonnalisation() {
        return imageUploadUrl != null || designPredefiniId != null || textePersonnalise != null;
    }

    public void ajouterOption(String cle, String valeur) {
        if (this.optionsSupplementaires == null) {
            this.optionsSupplementaires = new HashMap<>();
        }
        this.optionsSupplementaires.put(cle, valeur);
    }
}
