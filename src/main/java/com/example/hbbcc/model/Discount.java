package com.example.hbbcc.model;

import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.example.hbbcc.data.util.NumberToStringStorageHelper.*;

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
    public String getLongQuantity() {
        String stringQuantity = quantity.toString();
        checkStringSizeOfBigIntegerConvertion(stringQuantity);
        return stringQuantity;
    }

    public void setLongQuantity(String quantity) {
        this.quantity = new BigInteger(quantity);
    }

    @Column("PRICE")
    public String getLongPriceInCents() {
        String stringPrice = price.toPlainString();
        checkStringSizeOfBigDecimalConvertion(stringPrice);
        return stringPrice;
    }

    public void setLongPriceInCents(String stringPrice) {
        this.price = new BigDecimal(stringPrice);
    }

    Discount(BigInteger quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }
}
