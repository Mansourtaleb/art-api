package com.esprit.artdigital_backend.model.enums;

import lombok.Getter;

/**
 * Énumération des tailles de vêtements
 */
@Getter
public enum TailleVetement {
    XS("XS", "Très Petit"),
    S("S", "Petit"),
    M("M", "Moyen"),
    L("L", "Large"),
    XL("XL", "Très Large"),
    XXL("XXL", "Très Très Large"),
    XXXL("3XL", "3XL");

    private final String code;
    private final String libelle;

    TailleVetement(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return code;
    }
}