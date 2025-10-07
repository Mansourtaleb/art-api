package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Commande;
import com.esprit.artdigital_backend.model.enums.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends MongoRepository<Commande, String> {
    Page<Commande> findByClientId(String clientId, Pageable pageable);
    Page<Commande> findByStatut(StatutCommande statut, Pageable pageable);
    Page<Commande> findByClientIdAndStatut(String clientId, StatutCommande statut, Pageable pageable);
}