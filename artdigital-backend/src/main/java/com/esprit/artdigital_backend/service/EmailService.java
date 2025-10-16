package com.esprit.artdigital_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Base64;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${app.base-url:http://localhost:8080}")
    private String backendBaseUrl;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.email.fallback-to-console:true}")
    private boolean fallbackToConsole;

    @Value("${spring.mail.from:no-reply@printdigital.tn}")
    private String fromEmail;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // Génère un token URL-safe et suffisamment long pour la vérification par lien
    public String generateEmailVerificationToken() {
        byte[] bytes = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "✅ Vérification de votre compte Print&Digital";
        String verifyUrl = backendBaseUrl + "/api/auth/verify-email?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);

        String text = "Bonjour,\n\n"
                + "Bienvenue sur Print&Digital - Votre imprimerie digitale de confiance!\n\n"
                + "Cliquez sur le lien ci-dessous pour vérifier votre email :\n"
                + verifyUrl + "\n\n"
                + "Ce lien est valide pendant 24 heures.\n\n"
                + "Si vous n'avez pas créé de compte, ignorez ce message.\n\n"
                + "Cordialement,\nL'équipe Print&Digital";

        sendEmail(toEmail, subject, text, "Lien de vérification envoyé");
    }

    public void sendResetPasswordEmail(String toEmail, String code) {
        String subject = "🔐 Réinitialisation de votre mot de passe Print&Digital";
        String text = "Bonjour,\n\n"
                + "Vous avez demandé à réinitialiser votre mot de passe.\n\n"
                + "Votre code de réinitialisation est: " + code + "\n\n"
                + "Ce code est valide pendant 1 heure.\n\n"
                + "Si vous n'avez pas fait cette demande, ignorez ce message.\n\n"
                + "Cordialement,\nL'équipe Print&Digital";

        sendEmail(toEmail, subject, text, "Code de reset: " + code);
    }

    public void sendCommandeConfirmation(String toEmail, String commandeId, String clientNom) {
        String subject = "✅ Confirmation de commande #" + commandeId;
        String text = "Bonjour " + clientNom + ",\n\n"
                + "Votre commande #" + commandeId + " a été confirmée avec succès!\n\n"
                + "Vous pouvez suivre l'état de votre commande dans votre espace client:\n"
                + frontendUrl + "/mes-commandes\n\n"
                + "Merci de votre confiance!\n\n"
                + "Cordialement,\nL'équipe Print&Digital";

        sendEmail(toEmail, subject, text, "Commande confirmée: " + commandeId);
    }

    public void sendCommandeExpediee(String toEmail, String commandeId, String clientNom) {
        String subject = "📦 Votre commande a été expédiée #" + commandeId;
        String text = "Bonjour " + clientNom + ",\n\n"
                + "Bonne nouvelle! Votre commande #" + commandeId + " a été expédiée.\n\n"
                + "Vous devriez la recevoir sous 2-3 jours ouvrables.\n\n"
                + "Suivez votre commande: " + frontendUrl + "/mes-commandes/" + commandeId + "\n\n"
                + "Cordialement,\nL'équipe Print&Digital";

        sendEmail(toEmail, subject, text, "Commande expédiée: " + commandeId);
    }

    public void sendRetourDemande(String toEmail, String retourId, String clientNom) {
        String subject = "📋 Demande de retour enregistrée #" + retourId;
        String text = "Bonjour " + clientNom + ",\n\n"
                + "Votre demande de retour #" + retourId + " a été enregistrée.\n\n"
                + "Notre équipe va l'examiner dans les plus brefs délais.\n"
                + "Vous recevrez une réponse sous 48h.\n\n"
                + "Cordialement,\nL'équipe Print&Digital";

        sendEmail(toEmail, subject, text, "Retour demandé: " + retourId);
    }

    private void sendEmail(String toEmail, String subject, String text, String logMessage) {
        if (!emailEnabled) {
            log.info("📧 Email désactivé - {}", logMessage);
            return;
        }

        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(text);

                mailSender.send(message);
                log.info("✅ Email envoyé avec succès à {} - {}", toEmail, logMessage);
            } else {
                throw new IllegalStateException("MailSender non configuré");
            }
        } catch (Exception e) {
            // ✅ CORRECTION : Un seul catch pour toutes les exceptions
            log.error("❌ Erreur lors de l'envoi de l'email à {}: {}", toEmail, e.getMessage());

            if (fallbackToConsole) {
                log.info("📧 FALLBACK CONSOLE - Email pour {}", toEmail);
                log.info("Subject: {}", subject);
                log.info("Body:\n{}", text);
            }
        }
    }
}
