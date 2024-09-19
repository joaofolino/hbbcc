package com.example.hbbcc.service;

import com.example.hbbcc.model.Product;
import com.example.hbbcc.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.hbbcc.model.generator.ProductDataGenerator.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CheckoutServiceTest {

    @MockBean
    CatalogService catalogService;

    @Test void whenSingleFullPriceItemCheckout_expectSingleItemFullPriceTotal() throws ProductNotFoundException {
        Product product = buildSingleFullPriceProduct(true);
        when(catalogService.getProductById(Optional.of(product.getProductId()))).thenReturn(Optional.of(product));

        CheckoutService checkoutService = new CheckoutService(catalogService);
        Optional<BigDecimal> total = checkoutService.checkout(List.of(product.getProductId()));

        assertEquals(product.getPrice(), total.get());
    }

    @Test void whenMultiFullPriceItemCheckout_expectCombinedItemsFullPriceTotal() throws ProductNotFoundException {
        Collection<Product> products = buildMultiFullPriceProducts(true);
        Set<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toSet());
        when(catalogService.getProductsById(productIds)).thenReturn(products);

        CheckoutService checkoutService = new CheckoutService(catalogService);
        Optional<BigDecimal> total = checkoutService.checkout(productIds.stream().toList());

        assertEquals(products.stream().map(Product::getPrice).reduce(BigDecimal::add).get(), total.get());
    }

    ///TODO
    @Test void whenMaximumFullPriceItemCheckout_expectMaximumCombinedFullPriceTotal() {}

    @Test void whenSingleDiscountPriceItemCheckout_expectSingleItemDiscountPriceTotal() throws ProductNotFoundException {
        Product product = buildSingleDiscountPriceProduct(true);
        when(catalogService.getProductById(Optional.of(product.getProductId()))).thenReturn(Optional.of(product));

        CheckoutService checkoutService = new CheckoutService(catalogService);
        Optional<BigDecimal> total = checkoutService.checkout(List.of(product.getProductId()));

        assertEquals(product.getPrice(), total.get());
    }

    @Test void whenMultiDiscountPriceItemsCheckout_expectCombinedItemsDiscountPriceTotal() throws ProductNotFoundException {
        Collection<Product> products = buildMultiDiscountPriceProducts(true);
        Set<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toSet());
        when(catalogService.getProductsById(productIds)).thenReturn(products);

        CheckoutService checkoutService = new CheckoutService(catalogService);
        List<Long> productOrderIds = new LinkedList<>();
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        Optional<BigDecimal> total = checkoutService.checkout(productOrderIds);

        BigDecimal expectedTotal = new BigDecimal(200+120+80);
        assertEquals(expectedTotal, total.get());
    }

    ///TODO
    @Test void whenMaximumDiscountPriceItemsCheckout_expectMaximumItemsDiscountPriceTotal() {}

    @Test void whenSingleFullPriceAndDiscountPriceItemCheckout_expectCombinedSingleItemFullPriceWithSingleItemDiscountPriceTotal() throws ProductNotFoundException {
        Set<Product> products = new HashSet<>();
        products.add(buildSingleFullPriceProduct(true));
        products.add(buildSingleDiscountPriceProduct(true));
        Set<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toSet());
        when(catalogService.getProductsById(productIds)).thenReturn(products);

        CheckoutService checkoutService = new CheckoutService(catalogService);
        List<Long> productOrderIds = new LinkedList<>();
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        Optional<BigDecimal> total = checkoutService.checkout(productOrderIds);

        BigDecimal expectedTotal = new BigDecimal(200+3*50);
        assertEquals(expectedTotal, total.get());
    }

    @Test void whenMultiFullPriceAndDiscountPriceItemsCheckout_expectCombinedMultiItemsFullPriceAndDiscountPriceTotal() throws ProductNotFoundException {
        Collection<Product> products = buildCombinedFullPriceAndDiscountedProducts(true);
        Set<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toSet());
        when(catalogService.getProductsById(productIds)).thenReturn(products);

        CheckoutService checkoutService = new CheckoutService(catalogService);
        List<Long> productOrderIds = new LinkedList<>();
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        productOrderIds.addAll(productIds.stream().toList());
        Optional<BigDecimal> total = checkoutService.checkout(productOrderIds);

        BigDecimal expectedTotal = new BigDecimal(200+120+80+3*50+3*30);
        assertEquals(expectedTotal, total.get());
    }

    ///TODO
    @Test void whenMaximumFullPriceAndDiscountPriceItemsCheckout_expectCombinedMaximumItemsFullPriceAndDiscountPriceTotal() {}

    @Test void whenSingleUnknownItemCheckout_expectProductNotFoundException() {
        CheckoutService checkoutService = new CheckoutService(catalogService);
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.checkout(List.of(1L)));

        assertTrue(exception.getClass().isAssignableFrom(ProductNotFoundException.class));
    }
    @Test void whenMultiUnknownItemCheckout_expectProductNotFoundException() {
        CheckoutService checkoutService = new CheckoutService(catalogService);
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.checkout(List.of(1L, 2L)));

        assertTrue(exception.getClass().isAssignableFrom(ProductNotFoundException.class));
    }

}
