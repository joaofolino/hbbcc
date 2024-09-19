package com.example.hbbcc.data;

import com.example.hbbcc.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<Product, Long> {
}
