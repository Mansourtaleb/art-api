package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.filter.OeuvreFilterDTO;
import com.esprit.artdigital_backend.dto.request.OeuvreRequest;
import com.esprit.artdigital_backend.dto.response.OeuvreResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.exception.UnauthorizedException;
import com.esprit.artdigital_backend.model.Categorie;
import com.esprit.artdigital_backend.model.Oeuvre;
import com.esprit.artdigital_backend.model.embedded.AvisOeuvre;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.model.enums.StatutOeuvre;
import com.esprit.artdigital_backend.repository.CategorieRepository;
import com.esprit.artdigital_backend.repository.OeuvreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OeuvreService {

    @Autowired
    private OeuvreRepository oeuvreRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private CategorieRepository categorieRepository;

    public Oeuvre creerOeuvre(OeuvreRequest request, String artisteId) {
        // Vérifier que la catégorie existe
        Categorie categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        System.out.println("✅ Catégorie trouvée: " + categorie.getNom() + " (ID: " + categorie.getId() + ")");

        // Créer l'œuvre avec le NOM de la catégorie
        Oeuvre oeuvre = new Oeuvre(
                request.getTitre(),
                request.getDescription(),
                categorie.getNom(), // ← NOM de la catégorie
                request.getPrix(),
                request.getQuantiteDisponible(),
                artisteId,
                null
        );

        // ✅ CORRECTION : Ne plus associer categorieRef (supprimé du modèle)
        // oeuvre.setCategorieRef(categorie); ← LIGNE SUPPRIMÉE

        // Images
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            oeuvre.setImages(request.getImages());
        }

        // Statut
        if (request.getStatut() != null) {
            oeuvre.setStatut(request.getStatut());
        } else {
            oeuvre.setStatut(StatutOeuvre.PUBLIE);
        }

        Oeuvre saved = oeuvreRepository.save(oeuvre);
        System.out.println("✅ Sauvegardé: categorie=" + saved.getCategorie());

        return saved;
    }

    public Oeuvre getOeuvreById(String id) {
        return oeuvreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oeuvre non trouvée avec l'ID: " + id));
    }

    public Page<OeuvreResponse> getAllOeuvres(OeuvreFilterDTO filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(),
                Sort.by(Sort.Direction.DESC, "dateCreation"));

        Page<Oeuvre> oeuvres;

        if (filter.getArtisteId() != null) {
            oeuvres = oeuvreRepository.findByArtisteId(filter.getArtisteId(), pageable);
        } else if (filter.getCategorie() != null) {
            oeuvres = oeuvreRepository.findByCategorie(filter.getCategorie(), pageable);
        } else {
            oeuvres = oeuvreRepository.findAll(pageable);
        }

        // Filtrer par prix si spécifié
        if (filter.getPrixMin() != null || filter.getPrixMax() != null) {
            List<Oeuvre> filtered = oeuvres.getContent().stream()
                    .filter(o -> {
                        if (filter.getPrixMin() != null && o.getPrix().compareTo(filter.getPrixMin()) < 0) {
                            return false;
                        }
                        if (filter.getPrixMax() != null && o.getPrix().compareTo(filter.getPrixMax()) > 0) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(
                    filtered.stream().map(this::convertToResponse).collect(Collectors.toList()),
                    pageable,
                    filtered.size()
            );
        }

        return oeuvres.map(this::convertToResponse);
    }

    public Page<OeuvreResponse> getOeuvresByArtiste(String artisteId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
        return oeuvreRepository.findByArtisteId(artisteId, pageable).map(this::convertToResponse);
    }

    public Oeuvre updateOeuvre(String id, Oeuvre oeuvreDetails, String userId, RoleUtilisateur userRole) {
        Oeuvre oeuvre = getOeuvreById(id);

        if (userRole != RoleUtilisateur.ADMIN && !oeuvre.getArtisteId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier cette oeuvre");
        }

        if (oeuvreDetails.getTitre() != null) oeuvre.setTitre(oeuvreDetails.getTitre());
        if (oeuvreDetails.getDescription() != null) oeuvre.setDescription(oeuvreDetails.getDescription());
        if (oeuvreDetails.getCategorie() != null) oeuvre.setCategorie(oeuvreDetails.getCategorie());
        if (oeuvreDetails.getPrix() != null) oeuvre.setPrix(oeuvreDetails.getPrix());
        if (oeuvreDetails.getQuantiteDisponible() != null) oeuvre.setQuantiteDisponible(oeuvreDetails.getQuantiteDisponible());
        if (oeuvreDetails.getImages() != null) oeuvre.setImages(oeuvreDetails.getImages());
        if (oeuvreDetails.getStatut() != null) oeuvre.setStatut(oeuvreDetails.getStatut());

        return oeuvreRepository.save(oeuvre);
    }

    public void deleteOeuvre(String id, String userId, RoleUtilisateur userRole) {
        Oeuvre oeuvre = getOeuvreById(id);

        if (userRole != RoleUtilisateur.ADMIN && !oeuvre.getArtisteId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à supprimer cette oeuvre");
        }

        oeuvreRepository.delete(oeuvre);
    }

    public Oeuvre ajouterAvis(String id, String clientId, String clientNom, Integer note, String commentaire) {
        Oeuvre oeuvre = getOeuvreById(id);
        AvisOeuvre avis = new AvisOeuvre(clientId, clientNom, note, commentaire);
        oeuvre.ajouterAvis(avis);
        return oeuvreRepository.save(oeuvre);
    }

    public OeuvreResponse convertToResponse(Oeuvre oeuvre) {
        return new OeuvreResponse(
                oeuvre.getId(),
                oeuvre.getTitre(),
                oeuvre.getDescription(),
                oeuvre.getCategorie(),
                oeuvre.getPrix(),
                oeuvre.getQuantiteDisponible(),
                oeuvre.getArtisteId(),
                oeuvre.getArtisteNom(),
                oeuvre.getImages(),
                oeuvre.getStatut(),
                oeuvre.getDateCreation(),
                oeuvre.getAvis(),
                oeuvre.getNotemoyenne()
        );
    }
}