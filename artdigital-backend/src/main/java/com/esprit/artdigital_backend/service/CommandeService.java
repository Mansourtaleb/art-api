package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.filter.CommandeFilterDTO;
import com.esprit.artdigital_backend.dto.request.CommandeRequest;
import com.esprit.artdigital_backend.dto.response.CommandeResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.exception.UnauthorizedException;
import com.esprit.artdigital_backend.model.Commande;
import com.esprit.artdigital_backend.model.Oeuvre;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.embedded.ProduitCommande;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
import com.esprit.artdigital_backend.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private OeuvreService oeuvreService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Transactional
    public Commande creerCommande(CommandeRequest request, String userId) {
        Utilisateur client = utilisateurService.getUtilisateurById(userId);

        List<ProduitCommande> produits = new ArrayList<>();
        BigDecimal montantTotal = BigDecimal.ZERO;

        // Vérifier disponibilité et calculer total
        for (CommandeRequest.ProduitCommandeRequest produitReq : request.getProduits()) {
            Oeuvre oeuvre = oeuvreService.getOeuvreById(produitReq.getOeuvreId());

            // Vérifier stock
            if (oeuvre.getQuantiteDisponible() < produitReq.getQuantite()) {
                throw new IllegalArgumentException(
                        "Stock insuffisant pour " + oeuvre.getTitre() +
                                ". Disponible: " + oeuvre.getQuantiteDisponible()
                );
            }

            // Créer produit commande
            ProduitCommande produit = new ProduitCommande(
                    oeuvre.getId(),
                    oeuvre.getTitre(),
                    oeuvre.getPrix(),
                    produitReq.getQuantite(),
                    oeuvre.getImages().isEmpty() ? null : oeuvre.getImages().get(0)
            );
            produits.add(produit);

            // Calculer montant
            BigDecimal montantProduit = oeuvre.getPrix()
                    .multiply(BigDecimal.valueOf(produitReq.getQuantite()));
            montantTotal = montantTotal.add(montantProduit);

            // Déduire du stock
            oeuvre.setQuantiteDisponible(oeuvre.getQuantiteDisponible() - produitReq.getQuantite());
            oeuvreService.updateOeuvre(oeuvre.getId(), oeuvre, oeuvre.getArtisteId(), RoleUtilisateur.ADMIN);
        }

        // Créer commande
        Commande commande = new Commande(
                userId,
                client.getNom(),
                produits,
                montantTotal,
                request.getAdresseLivraison()
        );

        return commandeRepository.save(commande);
    }

    public Commande getCommandeById(String id, String userId, RoleUtilisateur userRole) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        // Vérifier autorisation
        if (userRole == RoleUtilisateur.CLIENT && !commande.getClientId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à voir cette commande");
        }

        return commande;
    }

    public Page<CommandeResponse> getAllCommandes(CommandeFilterDTO filter, String userId, RoleUtilisateur userRole) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(),
                Sort.by(Sort.Direction.DESC, "dateCommande"));

        Page<Commande> commandes;

        if (userRole == RoleUtilisateur.CLIENT) {
            // Client ne voit que ses commandes
            if (filter.getStatut() != null) {
                commandes = commandeRepository.findByClientIdAndStatut(userId, filter.getStatut(), pageable);
            } else {
                commandes = commandeRepository.findByClientId(userId, pageable);
            }
        } else {
            // Admin/Artiste voient tout
            if (filter.getClientId() != null && filter.getStatut() != null) {
                commandes = commandeRepository.findByClientIdAndStatut(filter.getClientId(), filter.getStatut(), pageable);
            } else if (filter.getClientId() != null) {
                commandes = commandeRepository.findByClientId(filter.getClientId(), pageable);
            } else if (filter.getStatut() != null) {
                commandes = commandeRepository.findByStatut(filter.getStatut(), pageable);
            } else {
                commandes = commandeRepository.findAll(pageable);
            }
        }

        return commandes.map(this::convertToResponse);
    }
    public Commande annulerCommande(String commandeId, String userId) {
        Commande commande = getCommandeById(commandeId, userId, RoleUtilisateur.CLIENT);

        // Vérifier que la commande appartient au client
        if (!commande.getClientId().equals(userId)) {
            throw new UnauthorizedException("Vous ne pouvez pas annuler cette commande");
        }

        // Vérifier que le statut permet l'annulation
        if (commande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Vous ne pouvez annuler que les commandes en attente"
            );
        }

        commande.setStatut(StatutCommande.ANNULEE);
        return commandeRepository.save(commande);
    }
    public Commande updateStatutCommande(String id, StatutCommande nouveauStatut,
                                         String userId, RoleUtilisateur userRole) {
        if (userRole == RoleUtilisateur.CLIENT) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier le statut");
        }

        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        commande.updateStatut(nouveauStatut);
        return commandeRepository.save(commande);
    }

    public CommandeResponse convertToResponse(Commande commande) {
        return new CommandeResponse(
                commande.getId(),
                commande.getClientId(),
                commande.getClientNom(),
                commande.getProduits(),
                commande.getMontantTotal(),
                commande.getAdresseLivraison(),
                commande.getStatut(),
                commande.getDateCommande(),
                commande.getDateModification()
        );
    }
}