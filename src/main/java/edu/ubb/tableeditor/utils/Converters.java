package edu.ubb.tableeditor.utils;

import edu.ubb.tableeditor.model.DecimalField;
import edu.ubb.tableeditor.model.Field;
import edu.ubb.tableeditor.model.IntegerField;

import java.util.Optional;

public final class Converters {

    private Converters() {
    }

    public static Optional<Field> tryParseNumberField(String key, String value) {
        try {
            return Optional.of(new IntegerField(key, Integer.parseInt(value)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Field> tryParseDecimalField(String key, String value) {
        try {
            return Optional.of(new DecimalField(key, Double.parseDouble(value)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
