package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Categorie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieRepository extends MongoRepository<Categorie, String> {
    List<Categorie> findByActifTrueOrderByOrdreAsc();
}