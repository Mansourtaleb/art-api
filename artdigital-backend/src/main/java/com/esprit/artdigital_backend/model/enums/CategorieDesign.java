package com.esprit.artdigital_backend.model.enums;

/**
 * Catégories de designs prédéfinis disponibles dans la bibliothèque
 */
public enum CategorieDesign {
    CITATION("Citations", "Citations motivantes, inspirantes, drôles"),
    AMOUR("Amour & Romance", "Designs pour couples, Saint-Valentin"),
    FAMILLE("Famille", "Maman, Papa, Enfants, Famille"),
    GAMING("Gaming", "Jeux vidéo, Esports, Geek"),
    SPORT("Sport", "Football, Basketball, Fitness"),
    HUMOUR("Humour", "Designs drôles, mèmes"),
    NATURE("Nature", "Fleurs, Animaux, Paysages"),
    ABSTRAIT("Abstrait", "Designs géométriques, artistiques"),
    MINIMALISTE("Minimaliste", "Designs simples, épurés"),
    VINTAGE("Vintage", "Style rétro, années 80/90"),
    PROFESSIONNEL("Professionnel", "Designs pour entreprises, logos"),
    ENFANTS("Enfants", "Dessins animés, personnages mignons"),
    RELIGION("Religion", "Designs religieux, spirituels"),
    PATRIOTIQUE("Patriotique", "Drapeau tunisien, fierté nationale"),
    AUTRES("Autres", "Designs divers");

    private final String libelle;
    private final String description;

    CategorieDesign(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
