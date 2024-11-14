package com.example.productapi.service;

import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Product product =
                Product.builder().id(1L)
                .name("Laptop")
                .price(1200.00)
                .description("A high-end laptop").build();
    }

    @Test
    void testGetAllProducts() {
        when(repository.findAll()).thenReturn(List.of(product));
        List<Product> products = service.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("Laptop", products.getFirst().getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> foundProduct = service.getProductById(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals("Laptop", foundProduct.get().getName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testCreateProduct() {
        when(repository.save(product)).thenReturn(product);
        Product createdProduct = service.createProduct(product);
        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getName());
        verify(repository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {

        Product updatedProduct = Product.builder()
                .id(null)
                .name("New Product")
                .price(122.22)
                .description("New product")
                .build();

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = service.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).save(any(Product.class));
    }
}
