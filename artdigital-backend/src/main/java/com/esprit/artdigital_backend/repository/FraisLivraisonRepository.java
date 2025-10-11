package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.FraisLivraison;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des frais de livraison
 */
@Repository
public interface FraisLivraisonRepository extends MongoRepository<FraisLivraison, String> {

    Optional<FraisLivraison> findByZoneIgnoreCase(String zone);

    List<FraisLivraison> findByActifTrue();

    List<FraisLivraison> findByActifTrueOrderByPrioriteDesc();

    List<FraisLivraison> findByActifTrueAndExpressDisponibleTrue();

    List<FraisLivraison> findByActifTrueAndMontantLivraisonGratuiteIsNotNull();

    List<FraisLivraison> findByTypeZoneAndActifTrue(String typeZone);
}
