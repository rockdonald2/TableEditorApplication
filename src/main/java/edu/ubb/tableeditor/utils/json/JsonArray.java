package edu.ubb.tableeditor.utils.json;

import java.util.*;

public class JsonArray extends JsonObject {

    private ArrayList<String> objects;

    public JsonArray(String arg) {
        super();
        Objects.requireNonNull(arg);

        arg = arg.trim();

        if (!arg.startsWith(JsonConstants.SQUARE_OPEN_BRACKETS.toString()) && !arg.endsWith(JsonConstants.SQUARE_CLOSE_BRACKETS.toString())) {
            throw new IllegalArgumentException("Failed to parse input string, not a valid JSONArray");
        }

        getJsonObjects(arg);
    }

    @Override
    protected void getJsonObjects(String arg) {
        objects = new ArrayList<>();

        StringBuilder builder = new StringBuilder(arg);

        builder.deleteCharAt(0);
        builder.deleteCharAt(builder.length() - 1);

        innerReplaces(builder);

        Collections.addAll(objects,
                Arrays.stream(
                                builder
                                        .toString()
                                        .split(String.valueOf(JsonConstants.COMMA)))
                        .map(String::trim)
                        .toList().toArray(new String[0]
                        )
        );
    }

    @Override
    protected void innerReplaces(StringBuilder arg) {
        boolean isObject = false;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);
            if (isObject && String.valueOf(a).compareTo(String.valueOf(JsonConstants.COMMA)) == 0) {
                arg.setCharAt(i, JsonConstants.SPECIAL.toChar());
            }

            if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.CURLY_OPEN_BRACKETS)) == 0) {
                isObject = true;
            }

            if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.CURLY_CLOSE_BRACKETS)) == 0) {
                isObject = false;
            }
        }
    }

    @Override
    public List<String> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> getRawValue(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<JsonArray> getJsonArray(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<JsonObject> getJsonObject(String key) {
        throw new UnsupportedOperationException();
    }

    public int length() {
        return objects.size();
    }

    public Optional<String> getRawValue(int index) {
        final String value = objects.get(index);

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value.replace(JsonConstants.SPECIAL.toChar(), JsonConstants.COMMA.toChar()).trim());
    }

    public Optional<JsonArray> getJsonArray(int index) {
        final String value = objects.get(index);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JsonArray(value.replace(JsonConstants.SPECIAL.toChar(), JsonConstants.COMMA.toChar())));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<JsonObject> getJsonObject(int index) {
        final String value = objects.get(index);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JsonObject(value.replace(JsonConstants.SPECIAL.toChar(), JsonConstants.COMMA.toChar())));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
