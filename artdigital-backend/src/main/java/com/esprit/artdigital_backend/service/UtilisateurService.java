package com.esprit.artdigital_backend.service;

import com.esprit.artdigital_backend.dto.response.UtilisateurResponse;
import com.esprit.artdigital_backend.exception.ResourceNotFoundException;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Utilisateur creerUtilisateur(String nom, String email, String motDePasse,
                                        RoleUtilisateur role, String telephone) {
        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        String encodedPassword = passwordEncoder.encode(motDePasse);
        Utilisateur utilisateur = new Utilisateur(nom, email, encodedPassword, role, telephone);
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getUtilisateurById(String id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }

    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }

    public Page<UtilisateurResponse> getAllUtilisateurs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateInscription"));
        return utilisateurRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Utilisateur updateUtilisateur(String id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = getUtilisateurById(id);

        if (utilisateurDetails.getNom() != null) {
            utilisateur.setNom(utilisateurDetails.getNom());
        }
        if (utilisateurDetails.getTelephone() != null) {
            utilisateur.setTelephone(utilisateurDetails.getTelephone());
        }
        if (utilisateurDetails.getPhotoProfile() != null) {
            utilisateur.setPhotoProfile(utilisateurDetails.getPhotoProfile());
        }
        if (utilisateurDetails.getDateNaissance() != null) {
            utilisateur.setDateNaissance(utilisateurDetails.getDateNaissance());
        }
        if (utilisateurDetails.getGenre() != null) {
            utilisateur.setGenre(utilisateurDetails.getGenre());
        }

        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(String id) {
        Utilisateur utilisateur = getUtilisateurById(id);
        utilisateurRepository.delete(utilisateur);
    }

    public void generateAndSendVerificationCode(String email) {
        Utilisateur utilisateur = getUtilisateurByEmail(email);

        String token = emailService.generateEmailVerificationToken();
        utilisateur.setCodeVerification(token);
        utilisateur.setCodeVerificationExpiration(LocalDateTime.now().plusHours(24));
        utilisateurRepository.save(utilisateur);

        emailService.sendVerificationEmail(email, token);
    }

    public Utilisateur verifyEmail(String code) {
        Utilisateur utilisateur = utilisateurRepository.findByCodeVerification(code)
                .orElseThrow(() -> new ResourceNotFoundException("Code de vérification invalide"));

        if (utilisateur.getCodeVerificationExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le code de vérification a expiré");
        }

        utilisateur.setEmailVerifie(true);
        utilisateur.setCodeVerification(null);
        utilisateur.setCodeVerificationExpiration(null);
        return utilisateurRepository.save(utilisateur);
    }

    public void generateResetCode(String email) {
        Utilisateur utilisateur = getUtilisateurByEmail(email);

        String code = emailService.generateVerificationCode();
        utilisateur.setResetToken(code);
        utilisateur.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        utilisateurRepository.save(utilisateur);

        emailService.sendResetPasswordEmail(email, code);
    }

    public void resetPassword(String code, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByResetToken(code)
                .orElseThrow(() -> new ResourceNotFoundException("Code de réinitialisation invalide"));

        if (utilisateur.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le code de réinitialisation a expiré");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateur.setResetToken(null);
        utilisateur.setResetTokenExpiration(null);
        utilisateurRepository.save(utilisateur);
    }
    public void changePassword(String userId, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = getUtilisateurById(userId);

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(ancienMotDePasse, utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }

        // Valider le nouveau mot de passe
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères");
        }

        // Mettre à jour le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }


    public UtilisateurResponse convertToResponse(Utilisateur utilisateur) {
        return new UtilisateurResponse(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getDateInscription(),
                utilisateur.getPhotoProfile(),
                utilisateur.getTelephone(),
                utilisateur.getDateNaissance(),
                utilisateur.getGenre(),
                utilisateur.getEmailVerifie(),
                utilisateur.getAdresses()
        );
    }
}
