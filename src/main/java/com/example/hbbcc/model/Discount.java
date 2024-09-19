package com.example.hbbcc.model;

import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.example.hbbcc.util.BigMath.*;

@Data
@AccessType(Type.PROPERTY)
@NoArgsConstructor
public class Discount {

    @Column("PRODUCT")
    private long productId;

    @Transient
    private BigInteger quantity;

    @Transient
    private BigDecimal price;

    @Column("QUANTITY")
    public long getLongQuantity() {
        checkBigIntegerCanBeAccuratelyConvertedToLong(quantity);
        return quantity.longValue();
    }

    public void setLongQuantity(long quantity) {
        this.quantity = BigInteger.valueOf(quantity);
    }

    @Column("PRICE")
    public long getLongPriceInCents() {
        BigDecimal priceInCents = price.multiply(BIG_DECIMAL_HUNDRED);
        checkBigDecimalCanBeAccuratelyConvertedToLong(priceInCents);
        return priceInCents.longValue();
    }

    public void setLongPriceInCents(long priceInCents) {
        this.price = BigDecimal.valueOf(priceInCents).divide(BIG_DECIMAL_HUNDRED);
    }

    Discount(BigInteger quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }
}
