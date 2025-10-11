package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.DesignPredifini;
import com.esprit.artdigital_backend.model.enums.CategorieDesign;
import com.esprit.artdigital_backend.model.enums.TypeProduit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des designs prédéfinis
 */
@Repository
public interface DesignPredifiniRepository extends MongoRepository<DesignPredifini, String> {

    List<DesignPredifini> findByActifTrue();

    List<DesignPredifini> findByFeaturedTrue();

    List<DesignPredifini> findByGratuitTrueAndActifTrue();

    List<DesignPredifini> findByGratuitFalseAndActifTrue();

    List<DesignPredifini> findByCategorie(CategorieDesign categorie);

    List<DesignPredifini> findByCategorieAndActifTrue(CategorieDesign categorie);

    List<DesignPredifini> findByProduitsCompatiblesContainingAndActifTrue(TypeProduit typeProduit);

    List<DesignPredifini> findTop20ByActifTrueOrderByScorePopulariteDesc();

    List<DesignPredifini> findByTagsContainingAndActifTrue(String tag);

    List<DesignPredifini> findByNomContainingIgnoreCaseAndActifTrue(String nom);

    List<DesignPredifini> findTop20ByActifTrueOrderByDateCreationDesc();

    List<DesignPredifini> findByActifTrueOrderByOrdreAsc();
}
