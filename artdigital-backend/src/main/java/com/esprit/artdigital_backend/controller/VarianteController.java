package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.model.VarianteProduit;
import com.esprit.artdigital_backend.service.VarianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/variantes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VarianteController {

    private final VarianteService varianteService;

    @PostMapping
    public ResponseEntity<VarianteProduit> creerVariante(@RequestBody VarianteProduit variante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(varianteService.creerVariante(variante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VarianteProduit> getVarianteById(@PathVariable String id) {
        return varianteService.getVarianteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<VarianteProduit> getVarianteBySku(@PathVariable String sku) {
        return varianteService.getVarianteBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<VarianteProduit>> getVariantesByProduit(@PathVariable String produitId) {
        return ResponseEntity.ok(varianteService.getVariantesByProduit(produitId));
    }

    @GetMapping("/produit/{produitId}/disponibles")
    public ResponseEntity<List<VarianteProduit>> getVariantesDisponibles(@PathVariable String produitId) {
        return ResponseEntity.ok(varianteService.getVariantesDisponibles(produitId));
    }

    @GetMapping
    public ResponseEntity<List<VarianteProduit>> getAllVariantes() {
        return ResponseEntity.ok(varianteService.getAllVariantes());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<VarianteProduit>> getVariantesFeatured() {
        return ResponseEntity.ok(varianteService.getVariantesFeatured());
    }

    @GetMapping("/stock-faible")
    public ResponseEntity<List<VarianteProduit>> getVariantesStockFaible(@RequestParam(defaultValue = "10") Integer seuil) {
        return ResponseEntity.ok(varianteService.getVariantesStockFaible(seuil));
    }

    @GetMapping("/populaires")
    public ResponseEntity<List<VarianteProduit>> getVariantesPopulaires() {
        return ResponseEntity.ok(varianteService.getVariantesPopulaires());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VarianteProduit> updateVariante(@PathVariable String id, @RequestBody VarianteProduit variante) {
        try {
            return ResponseEntity.ok(varianteService.updateVariante(id, variante));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<VarianteProduit> updateStock(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        try {
            Integer nouveauStock = body.get("stock");
            return ResponseEntity.ok(varianteService.updateStock(id, nouveauStock));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reserver-stock")
    public ResponseEntity<Map<String, Object>> reserverStock(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        Integer quantite = body.get("quantite");
        boolean success = varianteService.reserverStock(id, quantite);
        return ResponseEntity.ok(Map.of("success", success));
    }

    @PostMapping("/{id}/liberer-stock")
    public ResponseEntity<Void> libererStock(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        Integer quantite = body.get("quantite");
        varianteService.libererStock(id, quantite);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/confirmer-vente")
    public ResponseEntity<Map<String, Object>> confirmerVente(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        Integer quantite = body.get("quantite");
        boolean success = varianteService.confirmerVente(id, quantite);
        return ResponseEntity.ok(Map.of("success", success));
    }

    @PutMapping("/{id}/toggle-disponibilite")
    public ResponseEntity<VarianteProduit> toggleDisponibilite(@PathVariable String id) {
        try {
            return ResponseEntity.ok(varianteService.toggleDisponibilite(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariante(@PathVariable String id) {
        varianteService.deleteVariante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-sku/{sku}")
    public ResponseEntity<Map<String, Boolean>> checkSku(@PathVariable String sku) {
        return ResponseEntity.ok(Map.of("exists", varianteService.skuExists(sku)));
    }
}






