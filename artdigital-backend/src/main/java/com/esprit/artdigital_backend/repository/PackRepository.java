package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Pack;
import com.esprit.artdigital_backend.model.enums.Occasion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour la gestion des packs thématiques
 */
@Repository
public interface PackRepository extends MongoRepository<Pack, String> {

    List<Pack> findByActifTrue();

    List<Pack> findByFeaturedTrue();

    List<Pack> findByOccasion(Occasion occasion);

    List<Pack> findByOccasionAndActifTrue(Occasion occasion);

    List<Pack> findByActifTrueAndDateDebutBeforeAndDateFinAfter(
            LocalDateTime maintenant1,
            LocalDateTime maintenant2
    );

    List<Pack> findByActifTrueAndStockGreaterThan(Integer stock);

    List<Pack> findTop10ByActifTrueOrderByNombreVentesDesc();

    List<Pack> findByNomContainingIgnoreCase(String nom);

    List<Pack> findByActifTrueOrderByOrdreAsc();
}
