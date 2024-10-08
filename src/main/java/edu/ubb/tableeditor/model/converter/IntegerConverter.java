package edu.ubb.tableeditor.model.converter;

import edu.ubb.tableeditor.model.field.Field;
import edu.ubb.tableeditor.model.field.IntegerField;

import java.util.Optional;

public class IntegerConverter extends Converter {

    private Optional<Field> tryParseField(String key, String value) {
        try {
            return Optional.of(new IntegerField(key, Integer.parseInt(value)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Field> convert(String key, String value) {
        final var field = tryParseField(key, value);

        if (field.isEmpty()) {
            return convertNext(key, value);
        }

        return field;
    }

}
