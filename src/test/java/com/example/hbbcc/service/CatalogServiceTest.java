package com.example.hbbcc.service;

import com.example.hbbcc.data.CatalogRepository;
import com.example.hbbcc.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.hbbcc.model.checker.ProductDataChecker.assertRequestedMatchesRetrieved;
import static com.example.hbbcc.model.generator.ProductDataGenerator.buildCombinedFullPriceAndDiscountedProducts;
import static com.example.hbbcc.service.CatalogService.VALUE_MUST_NOT_BE_NULL;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatalogServiceTest {

    @MockBean
    CatalogRepository repository;

    @Test void whenSingleProductIsRequested_expectSingleProduct() {
        Collection<Product> products = buildCombinedFullPriceAndDiscountedProducts(false);

        Product firstProduct = products.stream().findFirst().get();
        Optional<Long> firstProductId = Optional.of(firstProduct.getProductId());

        when(repository.findById(firstProductId.get())).thenReturn(Optional.of(firstProduct));
        CatalogService catalogService = new CatalogService(repository);

        Optional<Product> serviceProduct = catalogService.getProductById(firstProductId);

        assertThat(serviceProduct).hasValue(firstProduct);
    }

    @Test void whenSingleProductIsRequested_withNoExistingProduct_expectEmpty() {
        Long productId = 1L;
        when(repository.findById(productId)).thenReturn(Optional.empty());
        CatalogService catalogService = new CatalogService(repository);

        Optional<Product> serviceProduct = catalogService.getProductById(Optional.of(productId));

        assertThat(serviceProduct).isEmpty();
    }

    @Test void whenNullProductIsRequested_expectIllegalArgumentException() {
        CatalogService catalogService = new CatalogService(repository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> catalogService.getProductById(null));
        String expectedMessage = String.format(VALUE_MUST_NOT_BE_NULL, "productId");
        String thrownMessage = exception.getMessage();
        assertEquals(expectedMessage, thrownMessage);
        assertTrue(exception.getClass().isAssignableFrom(IllegalArgumentException.class));
    }

    @Test void whenMultiProductsAreRequested_expectCollectionOfProducts() {
        Collection<Product> products = buildCombinedFullPriceAndDiscountedProducts(false);
        List<Long> productIds = products.stream().map(Product::getProductId).toList();

        when(repository.findAllById(productIds)).thenReturn(products);
        CatalogService catalogService = new CatalogService(repository);

        List<Long> requestedProductIds = products.stream().map(Product::getProductId).toList();
        Iterable<Product> serviceProducts = catalogService.getProductsById(requestedProductIds);

        assertRequestedMatchesRetrieved(products, serviceProducts);
    }

    @Test void whenMultiProductsAreRequested_withNoExistingProduct_expectEmpty() {
        Collection<Long> productIds = Set.of(1L);
        when(repository.findAllById(productIds)).thenReturn(Set.of());
        CatalogService catalogService = new CatalogService(repository);

        Iterable<Product> serviceProducts = catalogService.getProductsById(productIds);

        assertThat(serviceProducts).isEmpty();
    }

    @Test void whenNullProductsAreRequested_expectIllegalArgumentException() {
        CatalogService catalogService = new CatalogService(repository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> catalogService.getProductsById(null));
        String expectedMessage = String.format(VALUE_MUST_NOT_BE_NULL, "productIds");
        String thrownMessage = exception.getMessage();
        assertEquals(expectedMessage, thrownMessage);
        assertTrue(exception.getClass().isAssignableFrom(IllegalArgumentException.class));
    }

}
