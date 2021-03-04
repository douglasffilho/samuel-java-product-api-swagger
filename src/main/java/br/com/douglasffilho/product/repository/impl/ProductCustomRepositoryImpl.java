package br.com.douglasffilho.product.repository.impl;

import br.com.douglasffilho.product.domain.ProductPriceProjection;
import br.com.douglasffilho.product.repository.ProductCustomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final MongoTemplate mongoTemplate;

    public ProductCustomRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ProductPriceProjection> findProductPrices() {
        final ProjectionOperation projectionOperation = Aggregation.project("_id", "name", "value");

        final Aggregation aggregation = Aggregation.newAggregation(projectionOperation);

        return this.mongoTemplate.aggregate(
            aggregation,
            "products",
            ProductPriceProjection.class
        ).getMappedResults();
    }

}
