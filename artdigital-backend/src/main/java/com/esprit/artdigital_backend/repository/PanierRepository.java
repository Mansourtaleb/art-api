package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Panier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PanierRepository extends MongoRepository<Panier, String> {
    Optional<Panier> findByUtilisateurId(String utilisateurId);
    void deleteByUtilisateurId(String utilisateurId);
}