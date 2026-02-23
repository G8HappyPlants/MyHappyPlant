package com.example.myhappyplants.auxillary;

import java.util.regex.Pattern;

public class StringHelper {
    public static boolean canParseAsInt(String in) {
        return Pattern.matches("^-\\d+$", in);
    }

    public static boolean canParseAsDecimal(String in) {
        return Pattern.matches("^-\\d+[.\\d]*$", in);
    }
}
