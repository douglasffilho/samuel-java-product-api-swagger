package br.com.douglasffilho.product.domain;

import java.util.Optional;

public class Inventory {

    private Integer quantity;

    public Inventory() {

    }

    public Inventory(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        this.quantity = Optional.ofNullable(this.quantity).orElse(0);
        return this.quantity;
    }
}
