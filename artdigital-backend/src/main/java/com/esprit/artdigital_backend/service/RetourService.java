package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.exception.UnauthorizedException;
import com.esprit.artdigital_backend.model.Retour;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutRetour;
import com.esprit.artdigital_backend.repository.RetourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetourService {

    @Autowired
    private RetourRepository retourRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EmailService emailService;

    public Retour creerRetour(String commandeId, String motif, String description,
                              String produitsConcernes, String userId) {
        Utilisateur client = utilisateurService.getUtilisateurById(userId);

        Retour retour = new Retour(
                commandeId,
                userId,
                client.getNom(),
                client.getEmail(),
                motif,
                description,
                produitsConcernes
        );

        Retour saved = retourRepository.save(retour);

        // Envoyer email de confirmation
        emailService.sendRetourDemande(client.getEmail(), saved.getId(), client.getNom());

        return saved;
    }

    public Retour getRetourById(String id, String userId, RoleUtilisateur role) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retour non trouvé avec l'ID: " + id));

        // Vérifier autorisation
        if (role == RoleUtilisateur.CLIENT && !retour.getClientId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à voir ce retour");
        }

        return retour;
    }

    public List<Retour> getMesRetours(String userId) {
        return retourRepository.findByClientId(userId);
    }

    public Page<Retour> getAllRetours(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateDemande"));
        return retourRepository.findAllByOrderByDateDemandeDesc(pageable);
    }

    public List<Retour> getRetoursByStatut(StatutRetour statut) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "dateDemande"));
        return retourRepository.findByStatut(statut, pageable).getContent();
    }

    public Retour changerStatut(String id, StatutRetour nouveauStatut, String commentaire) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retour non trouvé avec l'ID: " + id));

        retour.changerStatut(nouveauStatut, commentaire);
        return retourRepository.save(retour);
    }

    public Retour accepterRetour(String id, String commentaire) {
        return changerStatut(id, StatutRetour.ACCEPTE, commentaire);
    }

    public Retour refuserRetour(String id, String commentaire) {
        return changerStatut(id, StatutRetour.REFUSE, commentaire);
    }

    public void deleteRetour(String id) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retour non trouvé avec l'ID: " + id));
        retourRepository.delete(retour);
    }
}