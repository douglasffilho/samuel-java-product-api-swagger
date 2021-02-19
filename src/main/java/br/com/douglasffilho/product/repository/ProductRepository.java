package br.com.douglasffilho.product.repository;

import br.com.douglasffilho.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
