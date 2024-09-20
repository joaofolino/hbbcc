package com.example.hbbcc.data.util;

public class NumberToStringStorageHelper {
    public static final String VALUE_TOO_LARGE_TO_CONVERT = "Value %s is larger than the set maximum.";


    public static void checkStringSizeOfBigIntegerConvertion(String convertedBigIntegerString) {
        if (convertedBigIntegerString.length() > 100) {
            throw new NumberFormatException(String.format(VALUE_TOO_LARGE_TO_CONVERT, convertedBigIntegerString));
        }
    }
    public static void checkStringSizeOfBigDecimalConvertion(String convertedBigDecimalString) {
        if (convertedBigDecimalString.length() > 100) {
            throw new NumberFormatException(String.format(VALUE_TOO_LARGE_TO_CONVERT, convertedBigDecimalString));
        }
    }
}
