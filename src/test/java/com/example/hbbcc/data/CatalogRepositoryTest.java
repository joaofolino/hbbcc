package com.example.hbbcc.data;

import com.example.hbbcc.model.Product;
import com.example.hbbcc.model.ProductDataGenerator;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Optional;

import static com.example.hbbcc.util.BigMath.BIG_DECIMAL_HUNDRED;
import static com.example.hbbcc.util.BigMath.VALUE_TOO_LARGE_TO_CONVERT;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CatalogConfiguration.class)
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
        Product singleFullPriceProduct = ProductDataGenerator.buildSingleFullPriceProduct();
        repository.save(singleFullPriceProduct);

        Optional<Product> optionalFullPriceProduct = repository.findById(singleFullPriceProduct.getProductId());
        assertThat(optionalFullPriceProduct).hasValue(singleFullPriceProduct);
    }

    @Test void whenMultiFullPriceProductsAreSearched_expectCollectionOfProductRecordsContainingNoDiscount() {
        Collection<Product> multiFullPriceProducts = ProductDataGenerator.buildMultiFullPriceProducts();
        repository.saveAll(multiFullPriceProducts);

        Iterable<Product> iterableMultiFullPriceProducts = repository.findAllById(
                multiFullPriceProducts.stream().map(Product::getProductId).toList());
        assertStoredMatchesRetrieved(multiFullPriceProducts, iterableMultiFullPriceProducts);
    }

    @Test void whenSingleDiscountedProductIsSearched_expectSingleProductRecordContainingDiscount() {
        Product singleDiscountPriceProduct = ProductDataGenerator.buildSingleDiscountPriceProduct();
        repository.save(singleDiscountPriceProduct);

        Optional<Product> optionalDiscountPriceProduct = repository.findById(singleDiscountPriceProduct.getProductId());
        assertThat(optionalDiscountPriceProduct).hasValue(singleDiscountPriceProduct);
    }

    @Test void whenMultiDiscountedProductsAreSearched_expectCollectionOfProductRecordsContainingDiscount() {
        Collection<Product> multiDiscountPriceProducts = ProductDataGenerator.buildMultiDiscountPriceProducts();
        repository.saveAll(multiDiscountPriceProducts);

        Iterable<Product> iterableMultiDiscountPriceProducts = repository.findAllById(
                multiDiscountPriceProducts.stream().map(Product::getProductId).toList());
        assertStoredMatchesRetrieved(multiDiscountPriceProducts, iterableMultiDiscountPriceProducts);
    }

    @Test void whenCombinedFullPriceAndDiscountedProductsAreSearched_expectCollectionOfProductRecordsWhereSomeContainDiscount() {
        Collection<Product> combinedFullPriceAndDiscountedProducts = ProductDataGenerator.buildCombinedFullPriceAndDiscountedProducts();
        repository.saveAll(combinedFullPriceAndDiscountedProducts);

        Iterable<Product> iterableCombinedFullPriceAndDiscountedProducts = repository.findAllById(
                combinedFullPriceAndDiscountedProducts.stream().map(Product::getProductId).toList());
        assertStoredMatchesRetrieved(combinedFullPriceAndDiscountedProducts, iterableCombinedFullPriceAndDiscountedProducts);
    }

    private static void assertStoredMatchesRetrieved(Collection<Product> multiDiscountPriceProducts,
                                                     Iterable<Product> iterableMultiDiscountPriceProducts) {
        assertThat(iterableMultiDiscountPriceProducts)
                .extracting(
                        Product::getProductId,
                        Product::getName,
                        Product::getPrice,
                        product -> product.getDiscounts().size())
                .containsExactlyElementsOf(
                        multiDiscountPriceProducts.stream().map(product -> new Tuple(
                                product.getProductId(),
                                product.getName(),
                                product.getPrice(),
                                product.getDiscounts().size())).toList());
    }

}
