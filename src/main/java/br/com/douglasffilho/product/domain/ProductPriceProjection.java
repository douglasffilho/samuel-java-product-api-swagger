package br.com.douglasffilho.product.domain;

import java.math.BigDecimal;

public class ProductPriceProjection {

    private String id;
    private String name;
    private BigDecimal value;

    public ProductPriceProjection() {

    }

    public ProductPriceProjection(String id, String name, BigDecimal value) {
        this.id = id;
        this.name = name;
        this.value = value;
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
}
