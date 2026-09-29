package com.spring.henallux.firstspringproject.service;

import com.spring.henallux.firstspringproject.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class CatalogService {
    // Catalogue de démonstration ; la lecture en base viendra au labo Hibernate.
    private final List<Product> products = List.of(
            new Product(1L, "CAL-001", "Calice impérial",
                    "Calice en acier brossé et émail noir pour la liturgie de l'Empire.",
                    new BigDecimal("320.00")),
            new Product(2L, "PAT-001", "Patène impériale",
                    "Patène en acier inoxydable ornée de l'emblème impérial.",
                    new BigDecimal("120.00")),
            new Product(3L, "ENC-001", "Encensoir de la Flotte",
                    "Encensoir en laiton vieilli pour les cérémonies de la Flotte.",
                    new BigDecimal("280.00"))
    );

    public List<Product> getProducts() {
        return products;
    }

    // Retourne null quand aucune référence ne correspond.
    public Product findByReference(String reference) {
        if (reference == null || reference.isBlank()) {
            return null;
        }
        String normalizedReference = reference.strip().toUpperCase(Locale.ROOT);
        for (Product product : products) {
            if (product.getReference().equals(normalizedReference)) {
                return product;
            }
        }
        return null;
    }

    public Product findById(Long id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}
