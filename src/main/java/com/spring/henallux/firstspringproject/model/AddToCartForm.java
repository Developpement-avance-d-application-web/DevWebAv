package com.spring.henallux.firstspringproject.model;

import jakarta.validation.constraints.*;

public class AddToCartForm {
    @NotNull(message = "Indique une quantité.")
    @Min(value = 1, message = "La quantité doit être au moins 1.")
    @Max(value = 99, message = "La quantité ne peut pas dépasser 99.")
    private Integer quantity;

    public AddToCartForm() {
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

}
