package br.com.douglasffilho.product.controller;

import br.com.douglasffilho.authorizationservice.annotations.IsAdministrator;
import br.com.douglasffilho.authorizationservice.annotations.IsManager;
import br.com.douglasffilho.product.domain.Product;
import br.com.douglasffilho.product.domain.ProductPriceProjection;
import br.com.douglasffilho.product.response.ProductCreatedResponse;
import br.com.douglasffilho.product.response.ProductDeletedResponse;
import br.com.douglasffilho.product.response.ProductFoundResponse;
import br.com.douglasffilho.product.response.ProductPricesFoundResponse;
import br.com.douglasffilho.product.response.ProductUpdatedResponse;
import br.com.douglasffilho.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("products")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @ApiOperation(
        value = "Create Product",
        nickname = "create",
        notes = "Creates a new product",
        response = ProductCreatedResponse.class,
        tags = {"Creation"}
    )
    @ApiResponses({
        @ApiResponse(code = 201, message = "product-created", response = ProductCreatedResponse.class),
        @ApiResponse(code = 400, message = "product-error"),
    })
    @ApiImplicitParams(@ApiImplicitParam(name = "Authorization", paramType = "header", format = "Bearer {token}"))
    @PostMapping
    @IsAdministrator
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreatedResponse create(@RequestBody Product product) {
        final Product created = this.service.createNewProduct(product);
        return new ProductCreatedResponse(created);
    }

    @ApiOperation(
        value = "Update Product",
        nickname = "update",
        notes = "Updates an existing product",
        response = ProductUpdatedResponse.class,
        tags = {"Update"}
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "product-updated", response = ProductUpdatedResponse.class),
        @ApiResponse(code = 400, message = "product-error"),
        @ApiResponse(code = 404, message = "product-not-found", response = ProductFoundResponse.class),
    })
    @ApiImplicitParams(@ApiImplicitParam(name = "Authorization", paramType = "header", format = "Bearer {token}"))
    @IsManager
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductUpdatedResponse update(@ApiParam(name = "product id") @PathVariable String id, @RequestBody Product product) {
        final Product updated = this.service.updateProduct(id, product);
        return new ProductUpdatedResponse(updated);
    }

    @ApiOperation(
        value = "Find Product by Id",
        nickname = "find",
        notes = "Finds an existing product by id",
        response = ProductFoundResponse.class,
        tags = {"Find"}
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "product-found", response = ProductFoundResponse.class),
        @ApiResponse(code = 404, message = "product-not-found", response = ProductFoundResponse.class),
    })
    @ApiImplicitParams(@ApiImplicitParam(name = "Authorization", paramType = "header", format = "Bearer {token}"))
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductFoundResponse find(@ApiParam(name = "product id") @PathVariable String id) {
        final Product found = this.service.findById(id);
        return new ProductFoundResponse(found);
    }

    @ApiOperation(
        value = "Delete Product by Id",
        nickname = "delete",
        notes = "Deletes an existing product by id",
        response = ProductDeletedResponse.class,
        tags = {"Delete"}
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "product-found", response = ProductDeletedResponse.class),
        @ApiResponse(code = 404, message = "product-not-found", response = ProductFoundResponse.class),
    })
    @ApiImplicitParams(@ApiImplicitParam(name = "Authorization", paramType = "header", format = "Bearer {token}"))
    @IsAdministrator
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDeletedResponse delete(@ApiParam(name = "product id") @PathVariable String id) {
        final Product found = this.service.deleteById(id);
        return new ProductDeletedResponse(found);
    }

    @ApiOperation(
        value = "List Products Prices",
        nickname = "list-prices",
        notes = "List All Products Prices with name and id",
        response = ProductPricesFoundResponse.class,
        tags = {"Find"}
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "product-prices-found", response = ProductPricesFoundResponse.class),
    })
    @ApiImplicitParams(@ApiImplicitParam(name = "Authorization", paramType = "header", format = "Bearer {token}"))
    @IsManager
    @GetMapping("/prices")
    @ResponseStatus(HttpStatus.OK)
    public ProductPricesFoundResponse listPrices() {
        List<ProductPriceProjection> prices = this.service.listPrices();
        return new ProductPricesFoundResponse(prices);
    }
}
