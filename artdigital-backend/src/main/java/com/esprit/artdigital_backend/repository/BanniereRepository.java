package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.Banniere;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BanniereRepository extends MongoRepository<Banniere, String> {
    List<Banniere> findByActifTrueOrderByOrdreAsc();
    List<Banniere> findByActifTrueAndDateDebutBeforeAndDateFinAfterOrderByOrdreAsc(
            LocalDateTime now1, LocalDateTime now2);
}