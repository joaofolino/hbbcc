package com.example.hbbcc.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.example.hbbcc.util.BigMath.BIG_DECIMAL_HUNDRED;
import static com.example.hbbcc.util.BigMath.checkBigDecimalCanBeAccuratelyConvertedToLong;

@Data
@AccessType(AccessType.Type.PROPERTY)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Product {

    @Id
    @Column("PRODUCT_ID")
    private long productId;

    private String name;

    @Transient
    private BigDecimal price;

    @MappedCollection(keyColumn = "QUANTITY")
    private final @AccessType(AccessType.Type.FIELD) @With(AccessLevel.PACKAGE) Map<BigInteger, Discount> discounts;

    public Product() {
        discounts = new HashMap<>();
    }

    @Column("PRICE")
    public long getLongPriceInCents() {
        BigDecimal priceInCents = price.multiply(BIG_DECIMAL_HUNDRED);
        checkBigDecimalCanBeAccuratelyConvertedToLong(priceInCents);
        return priceInCents.longValue();
    }

    public void setLongPriceInCents(long priceInCents) {
        price = BigDecimal.valueOf(priceInCents).divide(BIG_DECIMAL_HUNDRED);
    }

    public void addDiscount(BigInteger quantity, BigDecimal price) {
        Discount discount = new Discount(quantity, price);
        discounts.put(quantity, discount);
    }

}
