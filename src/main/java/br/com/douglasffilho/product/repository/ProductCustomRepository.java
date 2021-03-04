package br.com.douglasffilho.product.repository;

import br.com.douglasffilho.product.domain.ProductPriceProjection;

import java.util.List;

public interface ProductCustomRepository {

    List<ProductPriceProjection> findProductPrices();

}
