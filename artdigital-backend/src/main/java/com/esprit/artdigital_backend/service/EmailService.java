package com.esprit.artdigital_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.email.fallback-to-console:true}")
    private boolean fallbackToConsole;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationEmail(String toEmail, String code) {
        String subject = "✅ Vérification de votre compte Art Digital";
        String text = "Bonjour,\n\n"
                + "Bienvenue sur Art Digital!\n\n"
                + "Votre code de vérification est: " + code + "\n\n"
                + "Ce code est valide pendant 24 heures.\n\n"
                + "Si vous n'avez pas créé de compte, ignorez ce message.\n\n"
                + "Cordialement,\nL'équipe Art Digital";

        sendEmail(toEmail, subject, text, "Code de vérification: " + code);
    }

    public void sendResetPasswordEmail(String toEmail, String code) {
        String subject = "🔐 Réinitialisation de votre mot de passe Art Digital";
        String text = "Bonjour,\n\n"
                + "Vous avez demandé à réinitialiser votre mot de passe.\n\n"
                + "Votre code de réinitialisation est: " + code + "\n\n"
                + "Ce code est valide pendant 1 heure.\n\n"
                + "Si vous n'avez pas fait cette demande, ignorez ce message.\n\n"
                + "Cordialement,\nL'équipe Art Digital";

        sendEmail(toEmail, subject, text, "Code de reset: " + code);
    }

    private void sendEmail(String toEmail, String subject, String text, String logMessage) {
        if (!emailEnabled) {
            log.info("📧 EMAILS DÉSACTIVÉS - {} pour {}", logMessage, toEmail);
            return;
        }

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(text);
                message.setFrom("no-reply@artdigital.com");

                mailSender.send(message);
                log.info("✅ Email envoyé à {}: {}", toEmail, subject);
                return;
            } catch (MailException ex) {
                log.warn("❌ Erreur SMTP pour {}: {}", toEmail, ex.getMessage());
            }
        }

        if (fallbackToConsole) {
            log.info("📧 EMAIL SIMULÉ - {} pour {}", logMessage, toEmail);
            log.info("=== EMAIL SIMULÉ ===");
            log.info("À: {}", toEmail);
            log.info("Sujet: {}", subject);
            log.info("Contenu: {}", text.replace("\n", " | "));
            log.info("====================");
        }
    }
}