package com.esprit.artdigital_backend.model.enums;

/**
 * Modes de paiement acceptés par ArtStore Bizerte
 */
public enum ModePaiement {
    CASH_LIVRAISON("Paiement à la livraison", "💵", true),
    D17("Paiement D17", "💳", true),
    CARTE_BANCAIRE("Carte bancaire en ligne", "💳", true),
    VIREMENT("Virement bancaire", "🏦", false),
    WHATSAPP("Commander sur WhatsApp", "📱", true);

    private final String libelle;
    private final String icone;
    private final boolean actif;

    ModePaiement(String libelle, String icone, boolean actif) {
        this.libelle = libelle;
        this.icone = icone;
        this.actif = actif;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getIcone() {
        return icone;
    }

    public boolean isActif() {
        return actif;
    }

    public boolean isPaiementEnLigne() {
        return this == D17 || this == CARTE_BANCAIRE;
    }

    public boolean isPaiementLivraison() {
        return this == CASH_LIVRAISON;
    }

    public static java.util.List<ModePaiement> getModesActifs() {
        java.util.List<ModePaiement> modes = new java.util.ArrayList<>();
        for (ModePaiement mode : values()) {
            if (mode.actif) {
                modes.add(mode);
            }
        }
        return modes;
    }

    @Override
    public String toString() {
        return icone + " " + libelle;
    }
}
