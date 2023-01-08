package edu.ubb.tableeditor.model.converter;

import edu.ubb.tableeditor.model.Field;
import edu.ubb.tableeditor.model.TextField;

import java.util.Objects;
import java.util.Optional;

public abstract class Converter {

    private Converter next;

    public static Converter link(Converter first, Converter... chain) {
        Objects.requireNonNull(first);

        Converter head = first;

        for (var next : chain) {
            head.next = next;
            head = next;
        }

        return first;
    }

    public abstract Optional<Field> convert(String key, String value);

    protected final Optional<Field> convertNext(String key, String value) {
        if (next == null) {
            return Optional.of(new TextField(key, value));
        }

        return next.convert(key, value);
    }

}
