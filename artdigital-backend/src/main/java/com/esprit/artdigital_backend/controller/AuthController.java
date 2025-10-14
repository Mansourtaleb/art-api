package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.config.JwtTokenProvider;
import com.esprit.artdigital_backend.dto.request.*;
import com.esprit.artdigital_backend.dto.response.AuthResponse;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.repository.UtilisateurRepository;
import com.esprit.artdigital_backend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        RoleUtilisateur role = request.getRole() != null ? request.getRole() : RoleUtilisateur.CLIENT;

        Utilisateur utilisateur = utilisateurService.creerUtilisateur(
                request.getNom(),
                request.getEmail(),
                request.getMotDePasse(),
                role,
                request.getTelephone()
        );

        utilisateurService.generateAndSendVerificationCode(utilisateur.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Inscription réussie. Un code de vérification a été envoyé à votre email.",
                "userId", utilisateur.getId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        Utilisateur user = userOpt.get();

        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasse())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        if (!user.getEmailVerifie()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "error", "Email non vérifié",
                    "message", "Veuillez vérifier votre email avant de vous connecter"
            ));
        }

        // ✅ CORRECTION : Convertir RoleUtilisateur en String
        String token = tokenProvider.generateToken(user.getId(), user.getRole().name(), user.getRole());
        AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getNom(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        Utilisateur utilisateur = utilisateurService.verifyEmail(code);

        return ResponseEntity.ok(Map.of(
                "message", "Email vérifié avec succès",
                "userId", utilisateur.getId()
        ));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        utilisateurService.generateAndSendVerificationCode(email);

        return ResponseEntity.ok(Map.of(
                "message", "Un nouveau code de vérification a été envoyé à votre email"
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        utilisateurService.generateResetCode(email);

        return ResponseEntity.ok(Map.of(
                "message", "Un code de réinitialisation a été envoyé à votre email"
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        utilisateurService.resetPassword(request.getCode(), request.getNouveauMotDePasse());

        return ResponseEntity.ok(Map.of(
                "message", "Mot de passe réinitialisé avec succès"
        ));
    }
}