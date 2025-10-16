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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

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
                "message", "Inscription réussie. Un lien de vérification a été envoyé à votre email.",
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
        String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());        AuthResponse response = new AuthResponse(
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

    // Vérification via lien cliquable depuis l'email
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmailLink(@RequestParam("token") String token) {
        try {
            Utilisateur utilisateur = utilisateurService.verifyEmail(token);
            String html = "<!doctype html><html lang='fr'><head><meta charset='utf-8'><title>Email vérifié</title>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                    "<style>body{font-family:Arial,Helvetica,sans-serif;background:#f6f7fb;color:#222;margin:0;padding:40px;}" +
                    ".card{max-width:520px;margin:40px auto;background:#fff;border-radius:12px;box-shadow:0 6px 24px rgba(0,0,0,.08);padding:28px;text-align:center;}" +
                    ".title{font-size:20px;margin:8px 0 12px;color:#0a7c2f;} .msg{color:#444;margin-bottom:22px;}" +
                    ".btn{display:inline-block;background:#0a7c2f;color:#fff;text-decoration:none;padding:10px 16px;border-radius:8px;}" +
                    "</style></head><body><div class='card'>" +
                    "<div class='title'>Email vérifié avec succès</div>" +
                    "<div class='msg'>Votre email (" + utilisateur.getEmail() + ") est maintenant confirmé.</div>" +
                    "<a class='btn' href='" + frontendUrl + "/auth/login'>Se connecter</a>" +
                    "</div></body></html>";
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(html);
        } catch (com.esprit.artdigital_backend.exception.ResourceNotFoundException e) {
            String html = "<!doctype html><html lang='fr'><head><meta charset='utf-8'><title>Lien invalide</title>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                    "<style>body{font-family:Arial,Helvetica,sans-serif;background:#f6f7fb;color:#222;margin:0;padding:40px;}" +
                    ".card{max-width:520px;margin:40px auto;background:#fff;border-radius:12px;box-shadow:0 6px 24px rgba(0,0,0,.08);padding:28px;text-align:center;}" +
                    ".title{font-size:20px;margin:8px 0 12px;color:#b00020;} .msg{color:#444;margin-bottom:22px;}" +
                    ".btn{display:inline-block;background:#1f2937;color:#fff;text-decoration:none;padding:10px 16px;border-radius:8px;}" +
                    "</style></head><body><div class='card'>" +
                    "<div class='title'>Lien de vérification invalide</div>" +
                    "<div class='msg'>Le lien est incorrect. Vous pouvez demander un nouvel email de vérification.</div>" +
                    "<a class='btn' href='" + frontendUrl + "/auth/register'>Retour</a>" +
                    "</div></body></html>";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(html);
        } catch (IllegalArgumentException e) {
            String html = "<!doctype html><html lang='fr'><head><meta charset='utf-8'><title>Lien expiré</title>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                    "<style>body{font-family:Arial,Helvetica,sans-serif;background:#f6f7fb;color:#222;margin:0;padding:40px;}" +
                    ".card{max-width:520px;margin:40px auto;background:#fff;border-radius:12px;box-shadow:0 6px 24px rgba(0,0,0,.08);padding:28px;text-align:center;}" +
                    ".title{font-size:20px;margin:8px 0 12px;color:#b45309;} .msg{color:#444;margin-bottom:22px;}" +
                    ".btn{display:inline-block;background:#1f2937;color:#fff;text-decoration:none;padding:10px 16px;border-radius:8px;}" +
                    "</style></head><body><div class='card'>" +
                    "<div class='title'>Lien de vérification expiré</div>" +
                    "<div class='msg'>Le lien a expiré. Veuillez renvoyer un nouveau lien de vérification.</div>" +
                    "<a class='btn' href='" + frontendUrl + "/auth/register'>Renvoyer</a>" +
                    "</div></body></html>";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(html);
        }
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
