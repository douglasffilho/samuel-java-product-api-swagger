package br.com.douglasffilho.product.response;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.http.HttpStatus;

public class ProductCreatedResponse extends Response<Product> {
    public ProductCreatedResponse(Product result) {
        super("product-created", "", HttpStatus.CREATED, result);
    }
}
