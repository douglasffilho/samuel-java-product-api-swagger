package br.com.douglasffilho.product.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("products")
public class Product {

    @Id
    private String id;

    private String name;

    private BigDecimal value;

    private String description;

    private Inventory inventory;

    public Product() {

    }

    public Product(String name, BigDecimal value, String description, Inventory inventory) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.inventory = inventory;
    }

    public Product(String id, String name, BigDecimal value, String description, Inventory inventory) {
        this(name, value, description, inventory);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
