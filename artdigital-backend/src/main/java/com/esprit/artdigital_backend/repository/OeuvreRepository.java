package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Oeuvre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OeuvreRepository extends MongoRepository<Oeuvre, String> {
    Page<Oeuvre> findByArtisteId(String artisteId, Pageable pageable);
    Page<Oeuvre> findByCategorie(String categorie, Pageable pageable);
}