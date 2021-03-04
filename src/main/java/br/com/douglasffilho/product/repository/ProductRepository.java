package br.com.douglasffilho.product.repository;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>, ProductCustomRepository {
}
