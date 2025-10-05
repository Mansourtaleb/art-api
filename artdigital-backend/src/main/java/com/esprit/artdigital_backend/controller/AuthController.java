package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.config.JwtTokenProvider;
import com.esprit.artdigital_backend.dto.request.*;
import com.esprit.artdigital_backend.dto.response.AuthResponse;
import com.esprit.artdigital_backend.model.Utilisateur;
import com.esprit.artdigital_backend.model.enums.RoleUtilisateur;
import com.esprit.artdigital_backend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        Utilisateur utilisateur = utilisateurService.verifyEmail(request.getCode());

        String token = tokenProvider.generateToken(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getRole()
        );

        return ResponseEntity.ok(new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getEmailVerifie()
        ));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationCode(@RequestBody Map<String, String> request) {
        utilisateurService.generateAndSendVerificationCode(request.get("email"));
        return ResponseEntity.ok(Map.of("message", "Code de vérification renvoyé"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        String token = tokenProvider.generateToken(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getRole()
        );

        return ResponseEntity.ok(new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getEmailVerifie()
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody RequestResetPasswordDTO request) {
        utilisateurService.generateResetCode(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Un code de réinitialisation a été envoyé à votre email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        utilisateurService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }
}