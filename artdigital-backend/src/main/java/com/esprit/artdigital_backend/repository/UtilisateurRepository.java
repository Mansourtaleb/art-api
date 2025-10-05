package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByCodeVerification(String code);
    Optional<Utilisateur> findByResetToken(String token);
}