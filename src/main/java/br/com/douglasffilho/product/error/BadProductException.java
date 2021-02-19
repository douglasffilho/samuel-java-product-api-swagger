package br.com.douglasffilho.product.error;

public class BadProductException extends BadRequestException {
    public BadProductException(String productName) {
        super(String.format("invalid product for name=%s", productName));
    }
}
