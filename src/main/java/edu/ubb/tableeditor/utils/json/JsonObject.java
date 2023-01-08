package edu.ubb.tableeditor.utils.json;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JsonObject {

    private HashMap<String, String> objects;

    protected JsonObject() {
    }

    public JsonObject(String arg) {
        Objects.requireNonNull(arg);

        arg = arg.trim();

        if (!arg.startsWith(JsonConstants.CURLY_OPEN_BRACKETS.toString()) && !arg.endsWith(JsonConstants.CURLY_CLOSE_BRACKETS.toString())) {
            throw new IllegalArgumentException("Failed to parse input string, not a valid JSONObject");
        }

        getJsonObjects(arg);
    }

    protected void getJsonObjects(String arg) {
        objects = new HashMap<>();

        StringBuilder builder = new StringBuilder(arg);
        builder.deleteCharAt(0);
        builder.deleteCharAt(builder.length() - 1);

        innerReplaces(builder);

        for (String objs : builder.toString().split(String.valueOf(JsonConstants.COMMA))) {
            String[] objectValue = objs.split(String.valueOf(JsonConstants.COLON), 2);

            if (objectValue.length == 2) {
                this.objects.put(
                        objectValue[0]
                                .replace(JsonConstants.SINGLE_QUOTE.toString(), "")
                                .replace(JsonConstants.DOUBLE_QUOTE.toString(), "")
                                .trim(),
                        objectValue[1]
                                .replace(JsonConstants.SINGLE_QUOTE.toString(), "")
                                .replace(JsonConstants.DOUBLE_QUOTE.toString(), "")
                                .trim()
                );
            } else {
                throw new IllegalArgumentException("Invalid JSON object found, key without value at " + objs);
            }
        }
    }

    protected void innerReplaces(StringBuilder arg) {
        int currObjectIdx = 0;
        int currArrayIdx = 0;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);

            boolean isComma = String.valueOf(a).compareTo(String.valueOf(JsonConstants.COMMA)) == 0;

            if (String.valueOf(a).compareTo(String.valueOf('\n')) == 0) {
                arg.deleteCharAt(i);
            }

            if ((currArrayIdx > 0 || currObjectIdx > 0) && isComma) {
                arg.setCharAt(i, JsonConstants.SPECIAL.toChar());
            }

            if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.SQUARE_OPEN_BRACKETS)) == 0) {
                currArrayIdx++;
            } else if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.SQUARE_CLOSE_BRACKETS)) == 0) {
                currArrayIdx--;
            }

            if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.CURLY_OPEN_BRACKETS)) == 0) {
                currObjectIdx++;
            } else if (String.valueOf(a).compareTo(String.valueOf(JsonConstants.CURLY_CLOSE_BRACKETS)) == 0) {
                currObjectIdx--;
            }
        }
    }

    public List<String> getKeys() {
        return objects.keySet().stream().toList();
    }

    public Optional<String> getRawValue(String key) {
        final String value = objects.get(key);

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value.replace(JsonConstants.SPECIAL.toChar(), JsonConstants.COMMA.toChar()).trim());
    }

    public Optional<JsonArray> getJsonArray(String key) {
        final String value = objects.get(key);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JsonArray(value.replace(JsonConstants.SPECIAL.toChar(), JsonConstants.COMMA.toChar())));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<JsonObject> getJsonObject(String key) {
        final String value = objects.get(key);

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
