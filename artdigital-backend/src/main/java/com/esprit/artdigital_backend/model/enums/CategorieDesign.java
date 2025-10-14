package com.esprit.artdigital_backend.model.enums;

import lombok.Getter;

/**
 * Énumération des catégories de designs
 */
@Getter
public enum CategorieDesign {
    ART_ABSTRAIT("Art Abstrait", "Designs abstraits et modernes"),
    GEOMETRIQUE("Géométrique", "Formes géométriques et patterns"),
    NATURE("Nature", "Fleurs, paysages, nature"),
    ANIMAL("Animal", "Animaux et créatures"),
    TYPOGRAPHIE("Typographie", "Citations et textes stylisés"),
    MINIMALISTE("Minimaliste", "Designs épurés et simples"),
    VINTAGE("Vintage", "Style rétro et vintage"),
    MODERNE("Moderne", "Style contemporain"),
    ENFANT("Enfant", "Designs pour enfants"),
    SPORT("Sport", "Thèmes sportifs"),
    MUSIQUE("Musique", "Thèmes musicaux"),
    GAMING("Gaming", "Jeux vidéo et gaming"),
    CINEMA("Cinéma", "Films et séries"),
    PROFESSIONNEL("Professionnel", "Designs corporate"),
    FETE("Fête", "Anniversaires et célébrations"),
    MARIAGE("Mariage", "Thème mariage"),
    NAISSANCE("Naissance", "Thème naissance"),
    HUMOUR("Humour", "Designs drôles"),
    INSPIRANT("Inspirant", "Citations inspirantes"),
    CALLIGRAPHIE("Calligraphie", "Calligraphie arabe/française");

    private final String nom;
    private final String description;

    CategorieDesign(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    @Override
    public String toString() {
        return nom;
    }
}