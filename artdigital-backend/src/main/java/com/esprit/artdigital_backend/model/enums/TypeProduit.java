package com.esprit.artdigital_backend.model.enums;

/**
 * Énumération des types de produits disponibles
 */
public enum TypeProduit {
    HOODIE("Hoodie", "Sweat à capuche personnalisable", true),
    T_SHIRT("T-Shirt", "T-shirt personnalisable", true),
    MUG("Mug", "Mug personnalisable", true),
    COUSSIN("Coussin", "Coussin avec photo", true),
    PORTE_CLES("Porte-clés", "Porte-clés personnalisé", true),
    STICKER("Sticker", "Sticker autocollant", false),
    TABLEAU("Tableau", "Tableau photo sur toile", false),
    CASQUETTE("Casquette", "Casquette personnalisée", true),
    SAC("Sac", "Sac tote bag personnalisé", true),
    LAMPE("Lampe", "Lampe photo 3D", false);

    private final String nom;
    private final String description;
    private final boolean vetement;

    TypeProduit(String nom, String description, boolean vetement) {
        this.nom = nom;
        this.description = description;
        this.vetement = vetement;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVetement() {
        return vetement;
    }

    /**
     * Vérifie si le produit nécessite une taille
     */
    public boolean necessiteTaille() {
        return vetement;
    }

    /**
     * Vérifie si le produit peut être personnalisé
     */
    public boolean estPersonnalisable() {
        return true; // Tous les produits sont personnalisables
    }

    @Override
    public String toString() {
        return nom;
    }
}