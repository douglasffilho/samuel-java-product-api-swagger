package br.com.douglasffilho.product.response;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.http.HttpStatus;

public class ProductUpdatedResponse extends Response<Product> {
    public ProductUpdatedResponse(Product result) {
        super("product-updated", "", HttpStatus.OK, result);
    }
}
