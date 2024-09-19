package com.example.hbbcc.model.generator;

import com.example.hbbcc.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

public class ProductDataGenerator {

    static Product buildDiscountRolex(boolean withId) {
        Product discountRolex = new Product();
        if (withId) {
            discountRolex.setProductId(1L);
        }
        discountRolex.setName("Rolex");
        discountRolex.setPrice(BigDecimal.valueOf(100));
        discountRolex.addDiscount(BigInteger.valueOf(3), BigDecimal.valueOf(200));
        return discountRolex;
    }

    static Product buildDiscountMichaelKors(boolean withId) {
        Product discountMichaelKors = new Product();
        if (withId) {
            discountMichaelKors.setProductId(2L);
        }
        discountMichaelKors.setName("Michael Kors");
        discountMichaelKors.setPrice(BigDecimal.valueOf(80));
        discountMichaelKors.addDiscount(BigInteger.valueOf(2), BigDecimal.valueOf(120));
        return discountMichaelKors;
    }

    static Product buildSwatch(boolean withId) {
        Product swatch = new Product();
        if (withId) {
            swatch.setProductId(3L);
        }
        swatch.setName("Swatch");
        swatch.setPrice(BigDecimal.valueOf(50));
        return swatch;
    }

    static Product buildCasio(boolean withId) {
        Product casio = new Product();
        if (withId) {
            casio.setProductId(4L);
        }
        casio.setName("Swatch");
        casio.setPrice(BigDecimal.valueOf(30));
        return casio;
    }

    public static Product buildSingleDiscountPriceProduct(boolean withId) {
        return buildDiscountRolex(withId);
    }

    public static Product buildSingleFullPriceProduct(boolean withId) {
        return buildSwatch(withId);
    }

    public static Collection<Product> buildMultiDiscountPriceProducts(boolean withId) {
        return Set.of(buildDiscountRolex(withId), buildDiscountMichaelKors(withId));
    }

    public static Collection<Product> buildMultiFullPriceProducts(boolean withId) {
        return Set.of(buildCasio(withId), buildSwatch(withId));
    }

    public static Collection<Product> buildCombinedFullPriceAndDiscountedProducts(boolean withId) {
        return Set.of(buildDiscountRolex(withId), buildDiscountMichaelKors(withId), buildCasio(withId), buildSwatch(withId));
    }
}
