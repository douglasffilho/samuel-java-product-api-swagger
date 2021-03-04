package br.com.douglasffilho.product.response;

import br.com.douglasffilho.product.domain.ProductPriceProjection;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ProductPricesFoundResponse extends Responses<ProductPriceProjection> {
    public ProductPricesFoundResponse(List<ProductPriceProjection> results) {
        super("product-prices-found", "", HttpStatus.OK, results);
    }
}
