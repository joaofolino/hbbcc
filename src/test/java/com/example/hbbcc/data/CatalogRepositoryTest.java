package com.example.hbbcc.data;

import com.example.hbbcc.model.Product;
import com.example.hbbcc.model.generator.ProductDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Optional;

import static com.example.hbbcc.model.checker.ProductDataChecker.assertRequestedMatchesRetrieved;
import static com.example.hbbcc.util.BigMath.BIG_DECIMAL_HUNDRED;
import static com.example.hbbcc.util.BigMath.VALUE_TOO_LARGE_TO_CONVERT;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CatalogConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureJdbc
public class CatalogRepositoryTest {

    @Autowired
    CatalogRepository repository;

    @Test void whenSingleFullPriceProductIsSearched_withPriceAtMaximumValue_expectSingleProductRecordContainingNoDiscount() {
        BigDecimal maxPrice = BigDecimal.valueOf(Long.MAX_VALUE).divide(BIG_DECIMAL_HUNDRED);
        Product overPricedProduct = new Product();
        overPricedProduct.setName("Over Priced Product");
        overPricedProduct.setPrice(maxPrice);
        repository.save(overPricedProduct);

        Optional<Product> optionalFullPriceProduct = repository.findById(overPricedProduct.getProductId());
        assertThat(optionalFullPriceProduct).hasValue(overPricedProduct);
    }

    @Test void whenSingleFullPriceProductIsSearched_withPriceOverMaximumValue_expectException() {
        BigDecimal overPrice = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE).divide(BIG_DECIMAL_HUNDRED);
        Product overPricedProduct = new Product();
        overPricedProduct.setName("Over Priced Product");
        overPricedProduct.setPrice(overPrice);

        Exception databaseException = assertThrows(DbActionExecutionException.class, () -> repository.save(overPricedProduct));
        Throwable causeThrowable = databaseException.getCause();
        String expectedMessage = String.format(VALUE_TOO_LARGE_TO_CONVERT, overPrice.multiply(BIG_DECIMAL_HUNDRED));
        String thrownMessage = causeThrowable.getMessage();
        assertEquals(expectedMessage, thrownMessage);
        assertTrue(causeThrowable.getClass().isAssignableFrom(NumberFormatException.class));
    }

    @Test void whenSingleDiscountPriceProductIsSearched_withQuantityAtMaximumValue_expectSingleProductRecordContainingDiscount() {
        BigInteger maxQuantity = BigInteger.valueOf(Long.MAX_VALUE);
        Product unreasonablyDiscountedProduct = new Product();
        unreasonablyDiscountedProduct.setName("Unreasonably Discounted Product");
        unreasonablyDiscountedProduct.setPrice(BigDecimal.TEN);
        unreasonablyDiscountedProduct.addDiscount(maxQuantity, BigDecimal.ONE);
        repository.save(unreasonablyDiscountedProduct);

        Optional<Product> optionalFullPriceProduct = repository.findById(unreasonablyDiscountedProduct.getProductId());
        assertThat(optionalFullPriceProduct).hasValue(unreasonablyDiscountedProduct);
    }

    @Test void whenSingleDiscountedProductIsSearched_withQuantityOverMaximumValue_expectException() {
        BigInteger maxQuantity = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        Product unreasonablyDiscountedProduct = new Product();
        unreasonablyDiscountedProduct.setName("Unreasonably Discounted Product");
        unreasonablyDiscountedProduct.setPrice(BigDecimal.TEN);
        unreasonablyDiscountedProduct.addDiscount(maxQuantity, BigDecimal.ONE);

        Exception databaseException = assertThrows(DbActionExecutionException.class, () -> repository.save(unreasonablyDiscountedProduct));
        Throwable causeThrowable = databaseException.getCause();
        String expectedMessage = String.format(VALUE_TOO_LARGE_TO_CONVERT, maxQuantity);
        String thrownMessage = causeThrowable.getMessage();
        assertEquals(expectedMessage, thrownMessage);
        assertTrue(causeThrowable.getClass().isAssignableFrom(NumberFormatException.class));
    }

    @Test void whenSingleFullPriceProductIsSearched_expectSingleProductRecordContainingNoDiscount() {
        Product singleFullPriceProduct = ProductDataGenerator.buildSingleFullPriceProduct(false);
        repository.save(singleFullPriceProduct);

        Optional<Product> optionalFullPriceProduct = repository.findById(singleFullPriceProduct.getProductId());
        assertThat(optionalFullPriceProduct).hasValue(singleFullPriceProduct);
    }

    @Test void whenMultiFullPriceProductsAreSearched_expectCollectionOfProductRecordsContainingNoDiscount() {
        Collection<Product> multiFullPriceProducts = ProductDataGenerator.buildMultiFullPriceProducts(false);
        repository.saveAll(multiFullPriceProducts);

        Iterable<Product> iterableMultiFullPriceProducts = repository.findAllById(
                multiFullPriceProducts.stream().map(Product::getProductId).toList());
        assertRequestedMatchesRetrieved(multiFullPriceProducts, iterableMultiFullPriceProducts);
    }

    @Test void whenSingleDiscountedProductIsSearched_expectSingleProductRecordContainingDiscount() {
        Product singleDiscountPriceProduct = ProductDataGenerator.buildSingleDiscountPriceProduct(false);
        repository.save(singleDiscountPriceProduct);

        Optional<Product> optionalDiscountPriceProduct = repository.findById(singleDiscountPriceProduct.getProductId());
        assertThat(optionalDiscountPriceProduct).hasValue(singleDiscountPriceProduct);
    }

    @Test void whenMultiDiscountedProductsAreSearched_expectCollectionOfProductRecordsContainingDiscount() {
        Collection<Product> multiDiscountPriceProducts = ProductDataGenerator.buildMultiDiscountPriceProducts(false);
        repository.saveAll(multiDiscountPriceProducts);

        Iterable<Product> iterableMultiDiscountPriceProducts = repository.findAllById(
                multiDiscountPriceProducts.stream().map(Product::getProductId).toList());
        assertRequestedMatchesRetrieved(multiDiscountPriceProducts, iterableMultiDiscountPriceProducts);
    }

    @Test void whenCombinedFullPriceAndDiscountedProductsAreSearched_expectCollectionOfProductRecordsWhereSomeContainDiscount() {
        Collection<Product> combinedFullPriceAndDiscountedProducts = ProductDataGenerator.buildCombinedFullPriceAndDiscountedProducts(false);
        repository.saveAll(combinedFullPriceAndDiscountedProducts);

        Iterable<Product> iterableCombinedFullPriceAndDiscountedProducts = repository.findAllById(
                combinedFullPriceAndDiscountedProducts.stream().map(Product::getProductId).toList());
        assertRequestedMatchesRetrieved(combinedFullPriceAndDiscountedProducts, iterableCombinedFullPriceAndDiscountedProducts);
    }

}
