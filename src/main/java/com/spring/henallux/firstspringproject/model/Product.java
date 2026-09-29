package com.spring.henallux.firstspringproject.model;

import java.math.BigDecimal;

// Article du catalogue : aucune donnée de saisie ou de session ici. Un article vendu : référence, nom, description, prix.
public class Product {
    private final Long id;
    private final String reference;
    private final String name;
    private final String description;
    private final BigDecimal unitPrice;

    public Product(Long id, String reference, String name,
                   String description, BigDecimal unitPrice) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
