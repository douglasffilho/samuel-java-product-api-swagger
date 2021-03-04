package br.com.douglasffilho.product.error;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(String id) {
        super(String.format("product not found by id=%s", id));
    }
}
