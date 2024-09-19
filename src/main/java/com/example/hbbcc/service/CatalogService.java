package com.example.hbbcc.service;

import com.example.hbbcc.data.CatalogRepository;
import com.example.hbbcc.model.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatalogService {
    public static final String VALUE_MUST_NOT_BE_NULL = "%s must not be null.";

    CatalogRepository repository;

    public CatalogService(CatalogRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a Product by Id.
     * @param productId must not be null.
     * @throws IllegalArgumentException if productId is null.
     * @return product for the requested id or Optional#empty() if none found.
     */
    public Optional<Product> getProductById(Optional<Long> productId) {
        if (productId == null) {
            throw new IllegalArgumentException(String.format(VALUE_MUST_NOT_BE_NULL, "productId"));
        }
        return repository.findById(productId.get());
    }

    /**
     * Retrieves products by Id. If some or all ids are not found, no entities are returned for these IDs.
     * Note that the order of elements in the result is not guaranteed.
     * @param productIds must not be null nor contain any null values.
     * @throws IllegalArgumentException in case the given productIds or one of its items is null.
     * @return guaranteed to be not null. The size can be equal or less than the number of given productIds.
     */
    public Iterable<Product> getProductsById(Iterable<Long> productIds) {
        if (productIds == null) {
            throw new IllegalArgumentException(String.format(VALUE_MUST_NOT_BE_NULL, "productIds"));
        }
        return repository.findAllById(productIds);
    }
}
