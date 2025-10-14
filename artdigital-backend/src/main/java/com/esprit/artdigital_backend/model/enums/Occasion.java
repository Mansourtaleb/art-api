package com.esprit.artdigital_backend.model.enums;

import lombok.Getter;

/**
 * Énumération des occasions pour les packs
 */
@Getter
public enum Occasion {
    MARIAGE("Mariage", "Pack complet pour mariage"),
    NAISSANCE("Naissance", "Pack pour annoncer une naissance"),
    ANNIVERSAIRE("Anniversaire", "Pack anniversaire festif"),
    ENTREPRISE("Entreprise", "Pack professionnel entreprise"),
    ETUDIANT("Étudiant", "Pack spécial étudiants"),
    STARTUP("Startup", "Pack pour startups"),
    EVENEMENT("Événement", "Pack pour événements divers"),
    SAINT_VALENTIN("Saint-Valentin", "Pack romantique"),
    FETE_MERES("Fête des Mères", "Pack cadeau fête des mères"),
    FETE_PERES("Fête des Pères", "Pack cadeau fête des pères"),
    RAMADAN("Ramadan", "Pack spécial Ramadan"),
    AID("Aïd", "Pack spécial Aïd"),
    RENTREE_SCOLAIRE("Rentrée scolaire", "Pack rentrée des classes"),
    NOEL("Noël", "Pack spécial Noël"),
    NOUVEL_AN("Nouvel An", "Pack réveillon"),
    AUTRE("Autre", "Autre occasion");

    private final String nom;
    private final String description;

    Occasion(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    @Override
    public String toString() {
        return nom;
    }
}