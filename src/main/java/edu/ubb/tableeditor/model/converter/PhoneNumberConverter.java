package edu.ubb.tableeditor.model.converter;

import edu.ubb.tableeditor.model.field.Field;
import edu.ubb.tableeditor.model.field.PhoneNumberField;

import java.util.Optional;

public class PhoneNumberConverter extends Converter {

    @Override
    public Optional<Field> convert(String key, String value) {
        final var field = tryParseField(key, value);

        if (field.isEmpty()) {
            return convertNext(key, value);
        }

        return field;
    }

    private Optional<Field> tryParseField(String key, String value) {
        if (!value.matches("\\+\\d{4,}")) {
            return Optional.empty();
        }

        return Optional.of(new PhoneNumberField(key, value));
    }

}
