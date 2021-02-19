package br.com.douglasffilho.product.response;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.http.HttpStatus;

public class ProductFoundResponse extends Response<Product> {
    public ProductFoundResponse(Product result) {
        super("product-found", "", HttpStatus.OK, result);
    }
}
