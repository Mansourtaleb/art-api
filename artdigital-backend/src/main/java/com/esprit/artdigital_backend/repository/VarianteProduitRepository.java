package com.esprit.artdigital_backend.repository;

import com.esprit.artdigital_backend.model.VarianteProduit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des variantes de produits
 */
@Repository
public interface VarianteProduitRepository extends MongoRepository<VarianteProduit, String> {

    Optional<VarianteProduit> findBySku(String sku);

    List<VarianteProduit> findByProduitId(String produitId);

    List<VarianteProduit> findByProduitIdAndDisponibleTrue(String produitId);

    List<VarianteProduit> findByStockLessThanEqualAndDisponibleTrue(Integer seuil);

    List<VarianteProduit> findByFeaturedTrue();

    List<VarianteProduit> findByProduitIdAndCouleur(String produitId, String couleur);

    List<VarianteProduit> findTop10ByDisponibleTrueOrderByNombreVentesDesc();

    boolean existsBySku(String sku);
}
