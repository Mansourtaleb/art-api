package com.esprit.artdigital_backend.model.enums;

import lombok.Getter;

/**
 * Énumération complète des types de produits pour Print&Digital
 */
@Getter
public enum TypeProduit {
    // Vêtements
    TSHIRT("T-Shirt", "Vêtements", "T-shirt personnalisé 100% coton"),
    POLO("Polo", "Vêtements", "Polo personnalisé"),
    SWEAT("Sweat-shirt", "Vêtements", "Sweat-shirt personnalisé"),
    HOODIE("Hoodie", "Vêtements", "Hoodie avec capuche personnalisé"),
    CASQUETTE("Casquette", "Vêtements", "Casquette personnalisée"),

    // Articles de bureau
    MUG("Mug", "Articles de bureau", "Mug personnalisé 330ml"),
    MUG_MAGIQUE("Mug Magique", "Articles de bureau", "Mug qui change de couleur à la chaleur"),
    STYLO("Stylo", "Articles de bureau", "Stylo personnalisé"),
    CAHIER("Cahier", "Articles de bureau", "Cahier/Carnet personnalisé"),
    AGENDA("Agenda", "Articles de bureau", "Agenda personnalisé"),
    CALENDRIER("Calendrier", "Articles de bureau", "Calendrier mural personnalisé"),
    BLOC_NOTES("Bloc-notes", "Articles de bureau", "Bloc-notes personnalisé"),
    PORTE_CLE("Porte-clés", "Articles de bureau", "Porte-clés personnalisé"),

    // Impression papier
    POSTER("Poster", "Impression papier", "Poster haute qualité"),
    TOILE("Toile", "Impression papier", "Impression sur toile tendue"),
    CARTE_POSTALE("Carte postale", "Impression papier", "Carte postale personnalisée"),
    CARTE_VISITE("Carte de visite", "Impression papier", "Carte de visite professionnelle"),
    FLYER("Flyer", "Impression papier", "Flyer publicitaire A5/A4"),
    BROCHURE("Brochure", "Impression papier", "Brochure pliée"),
    DEPLIANT("Dépliant", "Impression papier", "Dépliant 3 volets"),
    AFFICHE("Affiche", "Impression papier", "Affiche grand format"),
    BANNIERE("Bannière", "Impression papier", "Bannière publicitaire"),

    // Événements
    FAIRE_PART("Faire-part", "Événements", "Faire-part mariage/naissance"),
    INVITATION("Invitation", "Événements", "Invitation événement"),
    MENU("Menu", "Événements", "Menu de table"),
    MARQUE_PLACE("Marque-place", "Événements", "Marque-place personnalisé"),
    ETIQUETTE("Étiquette", "Événements", "Étiquette adhésive personnalisée"),

    // Décoration
    STICKER("Sticker", "Décoration", "Sticker autocollant"),
    COUSSIN("Coussin", "Décoration", "Coussin décoratif personnalisé"),
    TABLEAU("Tableau", "Décoration", "Tableau encadré"),
    HORLOGE("Horloge", "Décoration", "Horloge murale personnalisée"),
    TAPIS_SOURIS("Tapis de souris", "Décoration", "Tapis de souris personnalisé"),

    // Cadeaux
    SAC_TOTE("Tote Bag", "Cadeaux", "Sac en toile personnalisé"),
    PUZZLE("Puzzle", "Cadeaux", "Puzzle personnalisé"),
    MAGNET("Magnet", "Cadeaux", "Magnet frigo personnalisé"),
    BADGE("Badge", "Cadeaux", "Badge/Pin's personnalisé"),

    // Emballage
    SACHET("Sachet", "Emballage", "Sachet kraft personnalisé"),
    BOITE("Boîte", "Emballage", "Boîte d'emballage personnalisée"),
    PAPIER_CADEAU("Papier cadeau", "Emballage", "Papier cadeau personnalisé");

    private final String nom;
    private final String categorie;
    private final String description;

    TypeProduit(String nom, String categorie, String description) {
        this.nom = nom;
        this.categorie = categorie;
        this.description = description;
    }

    @Override
    public String toString() {
        return nom;
    }
}