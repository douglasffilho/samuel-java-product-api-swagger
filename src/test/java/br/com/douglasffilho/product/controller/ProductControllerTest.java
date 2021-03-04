package br.com.douglasffilho.product.controller;

import br.com.douglasffilho.product.domain.Inventory;
import br.com.douglasffilho.product.domain.Product;
import br.com.douglasffilho.product.helpers.ProductCreatedResponseHelper;
import br.com.douglasffilho.product.helpers.ProductDeletedResponseHelper;
import br.com.douglasffilho.product.helpers.ProductErrorResponseHelper;
import br.com.douglasffilho.product.helpers.ProductFoundResponseHelper;
import br.com.douglasffilho.product.helpers.ProductNotFoundResponseHelper;
import br.com.douglasffilho.product.helpers.ProductUpdatedResponseHelper;
import br.com.douglasffilho.product.repository.ProductRepository;
import br.com.douglasffilho.product.response.ProductCreatedResponse;
import br.com.douglasffilho.product.response.ProductDeletedResponse;
import br.com.douglasffilho.product.response.ProductErrorResponse;
import br.com.douglasffilho.product.response.ProductFoundResponse;
import br.com.douglasffilho.product.response.ProductNotFoundResponse;
import br.com.douglasffilho.product.response.ProductUpdatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductRepository repository;

    @Test
    @WithMockUser(username = "test-user", authorities = {"ADMINISTRATOR"})
    public void testProductCreation() throws Exception {
        // given
        Product product = new Product(
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        when(this.repository.save(any(Product.class))).thenReturn(product);
        MvcResult result = mockMvc.perform(
            post("/api/products")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(1)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        ProductCreatedResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductCreatedResponseHelper.class);
        assertEquals("product-created", responseBody.getReason());
        assertEquals("", responseBody.getMessage());
        assertEquals(HttpStatus.CREATED, responseBody.getStatus());
        assertNotNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"ADMINISTRATOR"})
    public void testProductCreationThrowsBadProductException() throws Exception {
        // given
        Product product = new Product(
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        when(this.repository.save(any(Product.class))).thenThrow(new RuntimeException("invalid-product-name"));
        MvcResult result = mockMvc.perform(
            post("/api/products")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(1)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        ProductErrorResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductErrorResponseHelper.class);
        assertEquals("product-error", responseBody.getReason());
        assertEquals("invalid product for name=Product Test", responseBody.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"MANAGER"})
    public void testProductUpdate() throws Exception {
        // given
        Product product = new Product(
            "1",
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        when(this.repository.findById("1")).thenReturn(Optional.of(product));
        when(this.repository.save(any(Product.class))).thenReturn(product);
        MvcResult result = mockMvc.perform(
            put("/api/products/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(1)).findById("1");
        verify(this.repository, times(1)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        ProductUpdatedResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductUpdatedResponseHelper.class);
        assertEquals("product-updated", responseBody.getReason());
        assertEquals("", responseBody.getMessage());
        assertEquals(HttpStatus.OK, responseBody.getStatus());
        assertNotNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"MANAGER"})
    public void testProductUpdateThrowsBadProductException() throws Exception {
        // given
        Product product = new Product(
            "1",
            null,
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        when(this.repository.findById("1")).thenReturn(Optional.of(product));
        when(this.repository.save(any(Product.class))).thenThrow(new RuntimeException("invalid-product-name"));
        MvcResult result = mockMvc.perform(
            put("/api/products/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(1)).findById("1");
        verify(this.repository, times(1)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        ProductErrorResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductErrorResponseHelper.class);
        assertEquals("product-error", responseBody.getReason());
        assertEquals("invalid product for name=null", responseBody.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"MANAGER"})
    public void testProductUpdateNotMatchingIdThrowsBadProductException() throws Exception {
        // given
        Product product = new Product(
            "2",
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        MvcResult result = mockMvc.perform(
            put("/api/products/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(0)).findById(any(String.class));
        verify(this.repository, times(0)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        ProductErrorResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductErrorResponseHelper.class);
        assertEquals("product-error", responseBody.getReason());
        assertEquals("invalid product for name=Product Test", responseBody.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"MANAGER"})
    public void testProductUpdateNotValidIdThrowsBadProductException() throws Exception {
        // given
        Product product = new Product(
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        MvcResult result = mockMvc.perform(
            put("/api/products/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(0)).findById(any(String.class));
        verify(this.repository, times(0)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        ProductErrorResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductErrorResponseHelper.class);
        assertEquals("product-error", responseBody.getReason());
        assertEquals("invalid product for name=Product Test", responseBody.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"MANAGER"})
    public void testProductUpdateThrowsProductNotFoundException() throws Exception {
        // given
        Product product = new Product(
            "1",
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );
        String json = MAPPER.writeValueAsString(product);

        // when
        when(this.repository.findById("1")).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(
            put("/api/products/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // then
        verify(this.repository, times(1)).findById("1");
        verify(this.repository, times(0)).save(any(Product.class));
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        ProductNotFoundResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductNotFoundResponseHelper.class);
        assertEquals("product-not-found", responseBody.getReason());
        assertEquals("product not found by id=1", responseBody.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"CUSTOMER"})
    public void testFindProduct() throws Exception {
        // given
        Product product = new Product(
            "1",
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );

        // when
        when(this.repository.findById("1")).thenReturn(Optional.of(product));
        MvcResult result = mockMvc.perform(get("/api/products/1")).andReturn();

        // then
        verify(this.repository, times(1)).findById("1");
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        ProductFoundResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductFoundResponseHelper.class);
        assertEquals("product-found", responseBody.getReason());
        assertEquals("", responseBody.getMessage());
        assertEquals(HttpStatus.OK, responseBody.getStatus());
        assertNotNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"CUSTOMER"})
    public void testFindProductThrowsProductNotFoundException() throws Exception {
        // given
        String id = "1";

        // when
        when(this.repository.findById(id)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(get("/api/products/1")).andReturn();

        // then
        verify(this.repository, times(1)).findById(id);
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        ProductNotFoundResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductNotFoundResponseHelper.class);
        assertEquals("product-not-found", responseBody.getReason());
        assertEquals("product not found by id=1", responseBody.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"ADMINISTRATOR"})
    public void testDeleteProduct() throws Exception {
        // given
        Product product = new Product(
            "1",
            "Product Test",
            BigDecimal.TEN,
            "A test product",
            new Inventory(5)
        );

        // when
        when(this.repository.findById("1")).thenReturn(Optional.of(product));
        MvcResult result = mockMvc.perform(delete("/api/products/1")).andReturn();

        // then
        verify(this.repository, times(1)).findById("1");
        verify(this.repository, times(1)).deleteById("1");
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        ProductDeletedResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductDeletedResponseHelper.class);
        assertEquals("product-deleted", responseBody.getReason());
        assertEquals("", responseBody.getMessage());
        assertEquals(HttpStatus.OK, responseBody.getStatus());
        assertNotNull(responseBody.getResult());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"ADMINISTRATOR"})
    public void testDeleteProductThrowsProductNotFoundException() throws Exception {
        // given
        String id = "1";

        // when
        when(this.repository.findById(id)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(delete("/api/products/1")).andReturn();

        // then
        verify(this.repository, times(1)).findById(id);
        verify(this.repository, times(0)).deleteById("1");
        assertNotNull(result);
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        ProductNotFoundResponse responseBody = MAPPER.readValue(response.getContentAsString(), ProductNotFoundResponseHelper.class);
        assertEquals("product-not-found", responseBody.getReason());
        assertEquals("product not found by id=1", responseBody.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());
        assertNull(responseBody.getResult());
    }
}
