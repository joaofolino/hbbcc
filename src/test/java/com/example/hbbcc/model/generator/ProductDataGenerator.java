package com.example.hbbcc.model.generator;

import com.example.hbbcc.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

public class ProductDataGenerator {

    static Product buildDiscountRolex() {
        Product discountRolex = new Product();
        discountRolex.setName("Rolex");
        discountRolex.setPrice(BigDecimal.valueOf(100));
        discountRolex.addDiscount(BigInteger.valueOf(3), BigDecimal.valueOf(200));
        return discountRolex;
    }

    static Product buildDiscountMichaelKors() {
        Product discountMichaelKors = new Product();
        discountMichaelKors.setName("Michael Kors");
        discountMichaelKors.setPrice(BigDecimal.valueOf(80));
        discountMichaelKors.addDiscount(BigInteger.valueOf(2), BigDecimal.valueOf(120));
        return discountMichaelKors;
    }

    static Product buildSwatch() {
        Product swatch = new Product();
        swatch.setName("Swatch");
        swatch.setPrice(BigDecimal.valueOf(50));
        return swatch;
    }

    static Product buildCasio() {
        Product casio = new Product();
        casio.setName("Swatch");
        casio.setPrice(BigDecimal.valueOf(30));
        return casio;
    }

    public static Product buildSingleDiscountPriceProduct() {
        return buildDiscountRolex();
    }

    public static Product buildSingleFullPriceProduct() {
        return buildSwatch();
    }

    public static Collection<Product> buildMultiDiscountPriceProducts() {
        return Set.of(buildDiscountRolex(), buildDiscountMichaelKors());
    }

    public static Collection<Product> buildMultiFullPriceProducts() {
        return Set.of(buildCasio(), buildSwatch());
    }

    public static Collection<Product> buildCombinedFullPriceAndDiscountedProducts() {
        return Set.of(buildDiscountRolex(), buildDiscountMichaelKors(), buildCasio(), buildSwatch());
    }
}
