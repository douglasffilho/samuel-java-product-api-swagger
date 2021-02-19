package br.com.douglasffilho.product.response;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.http.HttpStatus;

public class ProductDeletedResponse extends Response<Product> {
    public ProductDeletedResponse(Product result) {
        super("product-deleted", "", HttpStatus.OK, result);
    }
}
