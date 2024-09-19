package com.example.hbbcc.service;

import com.example.hbbcc.model.Discount;
import com.example.hbbcc.model.Product;
import com.example.hbbcc.service.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
public class CheckoutService {

    CatalogService catalogService;

    CheckoutService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Calculates the total price of a given product checkout list, applying a single quantity discount.
     * Note that this method assumes only one discount per product may exist.
     * @param productIds a given list of products to checkout.
     * @return total price with discount.
     * @throws ProductNotFoundException if one or more products do not exist.
     */
    public Optional<BigDecimal> checkout(List<Long> productIds) throws ProductNotFoundException {
        Map<Long, Long> productQuantities = productIds.stream().collect(
                Collectors.groupingBy(identity(), Collectors.counting()));

        Set<Long> orderedProductIds = productQuantities.keySet();
        HashMap<Long, Product> orderedProducts = new HashMap<>();
        if (orderedProductIds.size() <= 1) {
            Optional<Product> optionalOrderedProduct = catalogService.getProductById(orderedProductIds.stream().findFirst());
            if (optionalOrderedProduct.isPresent()) {
                Product orderedProduct = optionalOrderedProduct.get();
                orderedProducts.put(orderedProduct.getProductId(), orderedProduct);
            }
        } else {
            Iterable<Product> iterableOrderedProducts = catalogService.getProductsById(orderedProductIds);
            iterableOrderedProducts.forEach(product -> orderedProducts.put(product.getProductId(), product));
        }


        if (orderedProducts.size() != productQuantities.size()) {
            throw new ProductNotFoundException();
        }

        return orderedProducts.values().stream().map(orderedProduct -> {
            BigDecimal productTotal;
            BigInteger quantity = BigInteger.valueOf(productQuantities.get(orderedProduct.getProductId()));

            Optional<Discount> optionalDiscount = orderedProduct.getDiscounts().values().stream().findFirst();
            if (optionalDiscount.isPresent()) {
                Discount discount = optionalDiscount.get();

                BigInteger numberOfDiscounts = quantity.divide(discount.getQuantity());
                BigDecimal discountPrice = discount.getPrice().multiply(new BigDecimal(numberOfDiscounts));

                BigInteger excessProducts = quantity.remainder(discount.getQuantity());
                BigDecimal excessPrice = orderedProduct.getPrice().multiply(new BigDecimal(excessProducts));

                productTotal = discountPrice.add(excessPrice);
            } else {
                productTotal = orderedProduct.getPrice().multiply(new BigDecimal(quantity));
            }

            return productTotal;
        }).reduce(BigDecimal::add);
    }
}
