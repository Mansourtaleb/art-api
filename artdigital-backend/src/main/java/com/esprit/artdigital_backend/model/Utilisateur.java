package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.embedded.AdresseLivraison;
import com.esprit.artdigital_backend.model.enums.Genre;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "utilisateurs")
public class Utilisateur {
    @Id
    private String id;

    private String nom;

    @Indexed(unique = true)
    private String email;

    private String motDePasse;

    private RoleUtilisateur role;

    private String telephone;

    private LocalDateTime dateInscription;

    private String photoProfile;

    private LocalDate dateNaissance;

    private Genre genre;

    private Boolean emailVerifie = false;

    private String codeVerification;

    private LocalDateTime codeVerificationExpiration;

    private String resetToken;

    private LocalDateTime resetTokenExpiration;

    private List<AdresseLivraison> adresses = new ArrayList<>();

    public Utilisateur(String nom, String email, String motDePasse, RoleUtilisateur role, String telephone) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.telephone = telephone;
        this.dateInscription = LocalDateTime.now();
        this.emailVerifie = false;
    }

    public void ajouterAdresse(AdresseLivraison adresse) {
        if (this.adresses == null) {
            this.adresses = new ArrayList<>();
        }

        // Si c'est la première adresse ou si elle est marquée par défaut
        if (this.adresses.isEmpty() || Boolean.TRUE.equals(adresse.getParDefaut())) {
            // Retirer le statut par défaut des autres adresses
            this.adresses.forEach(a -> a.setParDefaut(false));
            adresse.setParDefaut(true);
        }

        this.adresses.add(adresse);
    }

    public void supprimerAdresse(int index) {
        if (this.adresses != null && index >= 0 && index < this.adresses.size()) {
            boolean wasDefault = Boolean.TRUE.equals(this.adresses.get(index).getParDefaut());
            this.adresses.remove(index);

            // Si l'adresse supprimée était par défaut et qu'il reste des adresses
            if (wasDefault && !this.adresses.isEmpty()) {
                this.adresses.get(0).setParDefaut(true);
            }
        }
    }
}