package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.ProduitPersonnalise;
import com.esprit.artdigital_backend.model.enums.StatutProduitPersonnalise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitPersonnaliseRepository extends MongoRepository<ProduitPersonnalise, String> {
    Page<ProduitPersonnalise> findByClientId(String clientId, Pageable pageable);
    Page<ProduitPersonnalise> findByStatut(StatutProduitPersonnalise statut, Pageable pageable);
    Page<ProduitPersonnalise> findByTypeProduit(String typeProduit, Pageable pageable);
}