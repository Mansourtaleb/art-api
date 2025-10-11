package com.esprit.artdigital_backend.controller;

import com.esprit.artdigital_backend.model.FraisLivraison;
import com.esprit.artdigital_backend.service.FraisLivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/frais-livraison")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FraisLivraisonController {

    private final FraisLivraisonService fraisLivraisonService;

    @PostMapping
    public ResponseEntity<FraisLivraison> creerFrais(@RequestBody FraisLivraison frais) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fraisLivraisonService.creerFrais(frais));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraisLivraison> getFraisById(@PathVariable String id) {
        return fraisLivraisonService.getFraisById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/zone/{zone}")
    public ResponseEntity<FraisLivraison> getFraisByZone(@PathVariable String zone) {
        return fraisLivraisonService.getFraisByZone(zone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FraisLivraison>> getAllFrais() {
        return ResponseEntity.ok(fraisLivraisonService.getAllFrais());
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<FraisLivraison>> getFraisActifs() {
        return ResponseEntity.ok(fraisLivraisonService.getFraisActifs());
    }

    @GetMapping("/express-disponibles")
    public ResponseEntity<List<FraisLivraison>> getFraisExpressDisponibles() {
        return ResponseEntity.ok(fraisLivraisonService.getFraisExpressDisponibles());
    }

    @GetMapping("/livraison-gratuite")
    public ResponseEntity<List<FraisLivraison>> getFraisAvecLivraisonGratuite() {
        return ResponseEntity.ok(fraisLivraisonService.getFraisAvecLivraisonGratuite());
    }

    @GetMapping("/calculer")
    public ResponseEntity<Map<String, Object>> calculerFrais(
            @RequestParam String ville,
            @RequestParam BigDecimal montantCommande) {
        BigDecimal frais = fraisLivraisonService.calculerFrais(ville, montantCommande);
        BigDecimal fraisExpress = fraisLivraisonService.calculerFraisExpress(ville, montantCommande);
        boolean livraisonGratuite = fraisLivraisonService.estLivraisonGratuite(ville, montantCommande);
        BigDecimal montantRestant = fraisLivraisonService.montantRestantPourGratuit(ville, montantCommande);

        return ResponseEntity.ok(Map.of(
                "fraisStandard", frais,
                "fraisExpress", fraisExpress != null ? fraisExpress : 0,
                "livraisonGratuite", livraisonGratuite,
                "montantRestantPourGratuit", montantRestant != null ? montantRestant : 0
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FraisLivraison> updateFrais(@PathVariable String id, @RequestBody FraisLivraison frais) {
        try {
            return ResponseEntity.ok(fraisLivraisonService.updateFrais(id, frais));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/toggle-actif")
    public ResponseEntity<FraisLivraison> toggleActif(@PathVariable String id) {
        try {
            return ResponseEntity.ok(fraisLivraisonService.toggleActif(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrais(@PathVariable String id) {
        fraisLivraisonService.deleteFrais(id);
        return ResponseEntity.noContent().build();
    }
}
