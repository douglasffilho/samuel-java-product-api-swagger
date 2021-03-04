package br.com.douglasffilho.product.service;

import br.com.douglasffilho.product.domain.Product;
import br.com.douglasffilho.product.domain.ProductPriceProjection;
import br.com.douglasffilho.product.error.BadProductException;
import br.com.douglasffilho.product.error.ProductNotFoundException;
import br.com.douglasffilho.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Product createNewProduct(final Product product) {
        try {
            return this.repository.save(product);
        } catch (Exception ex) {
            log.info("error trying to create new product. {}", ex.getMessage());
            throw new BadProductException(product.getName());
        }
    }

    public Product updateProduct(final String id, final Product product) {
        if (product.getId() == null || !product.getId().equals(id))
            throw new BadProductException(product.getName());

        this.repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        try {
            return this.repository.save(product);
        } catch (Exception ex) {
            log.info("error trying to update product. {}", ex.getMessage());
            throw new BadProductException(product.getName());
        }
    }

    public Product findById(final String id) {
        return this.repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product deleteById(final String id) {
        Product product = this.repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        this.repository.deleteById(id);
        return product;
    }

    public List<ProductPriceProjection> listPrices() {
        return this.repository.findProductPrices();
    }
}
