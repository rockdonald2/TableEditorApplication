package edu.ubb.tableeditor.service.converter;

import edu.ubb.tableeditor.model.DecimalField;
import edu.ubb.tableeditor.model.Field;

import java.util.Optional;

public class DecimalConverter extends Converter {

    @Override
    public Optional<Field> convert(String key, String value) {
        final var field = tryParseField(key, value);

        if (field.isEmpty()) {
            return convertNext(key, value);
        }

        return field;
    }

    private Optional<Field> tryParseField(String key, String value) {
        try {
            return Optional.of(new DecimalField(key, Double.parseDouble(value)));
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
    }

}
