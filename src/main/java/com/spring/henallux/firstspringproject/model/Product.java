package com.spring.henallux.firstspringproject.model;

import java.math.BigDecimal;

public class Product {
    private Long id;

    private String reference;

    private String name;

    private String description;

    private BigDecimal unitPrice;

    private String categoryId;

    public Product() {
    }

    public Product(Long id, String reference, String name, String description, BigDecimal unitPrice, String categoryId) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

}
