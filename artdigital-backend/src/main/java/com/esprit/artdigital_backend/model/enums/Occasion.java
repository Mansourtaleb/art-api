package com.esprit.artdigital_backend.model.enums;

/**
 * Énumération des occasions/événements pour les produits et packs thématiques
 * Utilisé par ArtStore Bizerte pour gérer les produits saisonniers
 */
public enum Occasion {
    ST_VALENTIN("Saint-Valentin", 2, "❤️"),
    FETE_MERES("Fête des Mères", 5, "👩"),
    FETE_PERES("Fête des Pères", 6, "👨"),
    ANNIVERSAIRE("Anniversaire", 0, "🎂"),
    MARIAGE("Mariage", 0, "💍"),
    NAISSANCE("Naissance", 0, "👶"),
    NOEL("Noël", 12, "🎄"),
    EID("Aïd", 0, "🌙"),
    RAMADAN("Ramadan", 0, "🕌"),
    RENTREE_SCOLAIRE("Rentrée Scolaire", 9, "🎒"),
    DIPLOME("Diplôme", 6, "🎓"),
    RETRAITE("Retraite", 0, "🎉"),
    HALLOWEEN("Halloween", 10, "🎃"),
    TOUTE_OCCASION("Toute Occasion", 0, "✨");

    private final String libelle;
    private final int moisPrincipal;
    private final String emoji;

    Occasion(String libelle, int moisPrincipal, String emoji) {
        this.libelle = libelle;
        this.moisPrincipal = moisPrincipal;
        this.emoji = emoji;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getMoisPrincipal() {
        return moisPrincipal;
    }

    public String getEmoji() {
        return emoji;
    }

    public boolean estDeSaison(int moisActuel) {
        if (moisPrincipal == 0) {
            return true;
        }
        return moisActuel == moisPrincipal || moisActuel == (moisPrincipal - 1);
    }

    public static java.util.List<Occasion> occasionsDuMois(int mois) {
        java.util.List<Occasion> occasions = new java.util.ArrayList<>();
        for (Occasion occasion : values()) {
            if (occasion.estDeSaison(mois)) {
                occasions.add(occasion);
            }
        }
        return occasions;
    }

    @Override
    public String toString() {
        return libelle + " " + emoji;
    }
}
