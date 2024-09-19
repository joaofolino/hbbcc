package com.example.hbbcc.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigMath {
    public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);
    public static final BigInteger BIG_INTEGER_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    public static final BigDecimal BIG_DECIMAL_MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);

    public static final String VALUE_TOO_LARGE_TO_CONVERT = "Value %s is too large to convert accurately to a long.";


    public static void checkBigIntegerCanBeAccuratelyConvertedToLong(BigInteger bigInteger) {
        if (bigInteger.compareTo(BIG_INTEGER_MAX_LONG) > 0) {
            throw new NumberFormatException(String.format(VALUE_TOO_LARGE_TO_CONVERT, bigInteger));
        }
    }
    public static void checkBigDecimalCanBeAccuratelyConvertedToLong(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BIG_DECIMAL_MAX_LONG) > 0) {
            throw new NumberFormatException(String.format(VALUE_TOO_LARGE_TO_CONVERT, bigDecimal));
        }
    }
}
