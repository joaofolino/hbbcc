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

import static com.example.hbbcc.data.util.NumberToStringStorageHelper.checkStringSizeOfBigDecimalConvertion;

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
    public String getStringPrice() {
        String stringPrice = price.toPlainString();
        checkStringSizeOfBigDecimalConvertion(stringPrice);
        return stringPrice;
    }

    public void setStringPrice(String price) {
        this.price = new BigDecimal(price);
    }

    public void addDiscount(BigInteger quantity, BigDecimal price) {
        Discount discount = new Discount(quantity, price);
        discounts.put(quantity, discount);
    }

}
