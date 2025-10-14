package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.response.StatistiquesDTO;
import com.esprit.artdigital_backend.dto.response.StatistiquesDTO.ChiffreAffairesJourDTO;
import com.esprit.artdigital_backend.dto.response.StatistiquesDTO.ProduitVenteDTO;
import com.esprit.artdigital_backend.model.Commande;
import com.esprit.artdigital_backend.model.Oeuvre;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
import com.esprit.artdigital_backend.model.enums.StatutRetour;
import com.esprit.artdigital_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatistiquesService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private OeuvreRepository oeuvreRepository;

    @Autowired
    private RetourRepository retourRepository;

    public StatistiquesDTO getStatistiquesGlobales() {
        StatistiquesDTO stats = new StatistiquesDTO();

        // Récupérer toutes les commandes
        List<Commande> toutesCommandes = commandeRepository.findAll();

        // Filtrer commandes valides (non annulées)
        List<Commande> commandesValides = toutesCommandes.stream()
                .filter(c -> c.getStatut() != StatutCommande.ANNULEE)
                .collect(Collectors.toList());

        // Dates de référence
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debutMois = maintenant.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime debutSemaine = maintenant.minusDays(7);
        LocalDateTime debutJour = maintenant.withHour(0).withMinute(0).withSecond(0);

        // Chiffre d'affaires
        stats.setChiffreAffairesTotal(calculerCA(commandesValides));
        stats.setChiffreAffairesMois(calculerCAPeriode(commandesValides, debutMois));
        stats.setChiffreAffairesSemaine(calculerCAPeriode(commandesValides, debutSemaine));
        stats.setChiffreAffairesJour(calculerCAPeriode(commandesValides, debutJour));

        // Nombre de commandes
        stats.setNombreCommandesTotal((long) commandesValides.size());
        stats.setNombreCommandesMois(compterCommandesPeriode(commandesValides, debutMois));
        stats.setNombreCommandesSemaine(compterCommandesPeriode(commandesValides, debutSemaine));
        stats.setNombreCommandesJour(compterCommandesPeriode(commandesValides, debutJour));

        // Clients
        List<Utilisateur> clients = utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == RoleUtilisateur.CLIENT)
                .collect(Collectors.toList());
        stats.setNombreClients((long) clients.size());

        long nouveauxClients = clients.stream()
                .filter(u -> u.getDateInscription().isAfter(debutMois))
                .count();
        stats.setNombreNouveauxClientsMois(nouveauxClients);

        // Produits
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        stats.setNombreProduitsEnStock((long) oeuvres.size());
        stats.setNombreProduitsStockFaible(oeuvres.stream()
                .filter(o -> o.getQuantiteDisponible() < 10)
                .count());

        // Commandes par statut
        Map<String, Long> commandesParStatut = new HashMap<>();
        for (StatutCommande statut : StatutCommande.values()) {
            long count = toutesCommandes.stream()
                    .filter(c -> c.getStatut() == statut)
                    .count();
            commandesParStatut.put(statut.name(), count);
        }
        stats.setCommandesParStatut(commandesParStatut);

        // Top 10 produits
        stats.setTopProduits(getTopProduits(commandesValides));

        // Revenus par catégorie
        stats.setRevenusParCategorie(getRevenusParCategorie(commandesValides, oeuvres));

        // Évolution CA 7 derniers jours
        stats.setEvolutionCA(getEvolutionCA(commandesValides, 7));

        // Retours
        stats.setNombreRetours((long) retourRepository.findAll().size());
        stats.setNombreRetoursEnAttente((long) retourRepository.findByStatut(
                StatutRetour.DEMANDE,
                org.springframework.data.domain.PageRequest.of(0, 1000)
        ).getContent().size());

        return stats;
    }

    private BigDecimal calculerCA(List<Commande> commandes) {
        return commandes.stream()
                .map(Commande::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculerCAPeriode(List<Commande> commandes, LocalDateTime debut) {
        return commandes.stream()
                .filter(c -> c.getDateCommande().isAfter(debut))
                .map(Commande::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Long compterCommandesPeriode(List<Commande> commandes, LocalDateTime debut) {
        return commandes.stream()
                .filter(c -> c.getDateCommande().isAfter(debut))
                .count();
    }

    private List<ProduitVenteDTO> getTopProduits(List<Commande> commandes) {
        Map<String, ProduitVenteDTO> produitsMap = new HashMap<>();

        commandes.forEach(commande -> {
            commande.getProduits().forEach(produit -> {
                ProduitVenteDTO dto = produitsMap.getOrDefault(
                        produit.getOeuvreId(),
                        new ProduitVenteDTO(produit.getOeuvreId(), produit.getTitre(), 0L, BigDecimal.ZERO)
                );
                dto.setQuantiteVendue(dto.getQuantiteVendue() + produit.getQuantite());
                dto.setMontantTotal(dto.getMontantTotal().add(
                        produit.getPrix().multiply(BigDecimal.valueOf(produit.getQuantite()))
                ));
                produitsMap.put(produit.getOeuvreId(), dto);
            });
        });

        return produitsMap.values().stream()
                .sorted((a, b) -> Long.compare(b.getQuantiteVendue(), a.getQuantiteVendue()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> getRevenusParCategorie(List<Commande> commandes, List<Oeuvre> oeuvres) {
        Map<String, BigDecimal> revenusMap = new HashMap<>();
        Map<String, String> oeuvreCategories = oeuvres.stream()
                .collect(Collectors.toMap(Oeuvre::getId, Oeuvre::getCategorie));

        commandes.forEach(commande -> {
            commande.getProduits().forEach(produit -> {
                String categorie = oeuvreCategories.getOrDefault(produit.getOeuvreId(), "Autre");
                BigDecimal montant = produit.getPrix().multiply(BigDecimal.valueOf(produit.getQuantite()));
                revenusMap.merge(categorie, montant, BigDecimal::add);
            });
        });

        return revenusMap;
    }

    private List<ChiffreAffairesJourDTO> getEvolutionCA(List<Commande> commandes, int nombreJours) {
        LocalDate aujourdhui = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        List<ChiffreAffairesJourDTO> evolution = new ArrayList<>();

        for (int i = nombreJours - 1; i >= 0; i--) {
            LocalDate date = aujourdhui.minusDays(i);
            LocalDateTime debutJour = date.atStartOfDay();
            LocalDateTime finJour = date.plusDays(1).atStartOfDay();

            List<Commande> commandesJour = commandes.stream()
                    .filter(c -> c.getDateCommande().isAfter(debutJour) &&
                            c.getDateCommande().isBefore(finJour))
                    .collect(Collectors.toList());

            BigDecimal montant = calculerCA(commandesJour);

            evolution.add(new ChiffreAffairesJourDTO(
                    date.format(formatter),
                    montant,
                    (long) commandesJour.size()
            ));
        }

        return evolution;
    }
}