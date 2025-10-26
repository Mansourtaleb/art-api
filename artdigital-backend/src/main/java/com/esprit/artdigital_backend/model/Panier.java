package com.esprit.artdigital_backend.model;

import com.esprit.artdigital_backend.model.embedded.PanierItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "paniers")
public class Panier {
    @Id
    private String id;

    private String utilisateurId;
    private List<PanierItem> items = new ArrayList<>();
    private BigDecimal sousTotal = BigDecimal.ZERO;
    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification = LocalDateTime.now();

    public Panier(String utilisateurId) {
        this.utilisateurId = utilisateurId;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public void calculerSousTotal() {
        this.sousTotal = items.stream()
                .map(PanierItem::calculerSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.dateModification = LocalDateTime.now();
    }

    public void ajouterItem(PanierItem item) {
        PanierItem existant = items.stream()
                .filter(i -> i.getOeuvreId().equals(item.getOeuvreId()))
                .findFirst()
                .orElse(null);

        if (existant != null) {
            existant.setQuantite(existant.getQuantite() + item.getQuantite());
        } else {
            items.add(item);
        }

        calculerSousTotal();
    }

    public void updateQuantite(String oeuvreId, Integer nouvelleQuantite) {
        PanierItem item = items.stream()
                .filter(i -> i.getOeuvreId().equals(oeuvreId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé dans le panier"));

        if (nouvelleQuantite <= 0) {
            items.remove(item);
        } else {
            item.setQuantite(nouvelleQuantite);
        }

        calculerSousTotal();
    }

    public void retirerItem(String oeuvreId) {
        items.removeIf(i -> i.getOeuvreId().equals(oeuvreId));
        calculerSousTotal();
    }

    public void vider() {
        items.clear();
        sousTotal = BigDecimal.ZERO;
        dateModification = LocalDateTime.now();
    }

    public int getNombreItems() {
        return items.stream().mapToInt(PanierItem::getQuantite).sum();
    }
}