package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Retour;
import com.esprit.artdigital_backend.model.enums.StatutRetour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetourRepository extends MongoRepository<Retour, String> {
    List<Retour> findByClientId(String clientId);
    Page<Retour> findByStatut(StatutRetour statut, Pageable pageable);
    List<Retour> findByCommandeId(String commandeId);
    Page<Retour> findAllByOrderByDateDemandeDesc(Pageable pageable);
}