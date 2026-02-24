package com.example.myhappyplants.auxillary;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class StringHelper {
    public static Optional<Integer> tryParseInt(String in) {
        return Optional.ofNullable(in).map(s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    public static Optional<Double> tryParseDouble(String in) {
        return Optional.ofNullable(in).map(s -> {
            try {
                return Double.parseDouble(in);
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }
}
