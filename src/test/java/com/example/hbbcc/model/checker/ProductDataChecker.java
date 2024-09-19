package com.example.hbbcc.model.checker;

import com.example.hbbcc.model.Product;
import org.assertj.core.groups.Tuple;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductDataChecker {

    public static void assertRequestedMatchesRetrieved(Collection<Product> multiDiscountPriceProducts,
                                                        Iterable<Product> iterableMultiDiscountPriceProducts) {
        assertThat(iterableMultiDiscountPriceProducts)
                .extracting(
                        Product::getProductId,
                        Product::getName,
                        Product::getPrice,
                        product -> product.getDiscounts().size())
                .containsExactlyInAnyOrderElementsOf(
                        multiDiscountPriceProducts.stream().map(product -> new Tuple(
                                product.getProductId(),
                                product.getName(),
                                product.getPrice(),
                                product.getDiscounts().size())).toList());
    }
}
