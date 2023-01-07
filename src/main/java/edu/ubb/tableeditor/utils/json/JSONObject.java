package edu.ubb.tableeditor.utils.json;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JSONObject {

    private HashMap<String, String> objects;

    public JSONObject() {
    }

    public JSONObject(String arg) {
        Objects.requireNonNull(arg);

        arg = arg.trim();

        if (!arg.startsWith(JSONConstants.CURLY_OPEN_BRACKETS.toString()) && !arg.endsWith(JSONConstants.CURLY_CLOSE_BRACKETS.toString())) {
            throw new IllegalArgumentException("Failed to parse input string, not a valid JSONObject");
        }

        getJSONObjects(arg.trim());
    }

    protected void getJSONObjects(String arg) {
        objects = new HashMap<>();

        if (arg.startsWith(String.valueOf(JSONConstants.CURLY_OPEN_BRACKETS)) && arg.endsWith(String.valueOf(JSONConstants.CURLY_CLOSE_BRACKETS))) {
            StringBuilder builder = new StringBuilder(arg);
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);

            innerReplaces(builder);

            for (String objs : builder.toString().split(String.valueOf(JSONConstants.COMMA))) {
                String[] objectValue = objs.split(String.valueOf(JSONConstants.COLON), 2);

                if (objectValue.length == 2)
                    this.objects.put(
                            objectValue[0].replace("'", "").replace("\"", "").trim(),
                            objectValue[1].replace("'", "").replace("\"", "").trim()
                    );
            }
        }
    }

    protected void innerReplaces(StringBuilder arg) {
        boolean isJsonArray = false;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);

            if (isJsonArray && String.valueOf(a).compareTo(String.valueOf(JSONConstants.COMMA)) == 0) {
                arg.setCharAt(i, JSONConstants.SPECIAL.toChar());
            }

            if (String.valueOf(a).compareTo(String.valueOf(JSONConstants.SQUARE_OPEN_BRACKETS)) == 0) {
                isJsonArray = true;
            }

            if (String.valueOf(a).compareTo(String.valueOf(JSONConstants.SQUARE_CLOSE_BRACKETS)) == 0) {
                isJsonArray = false;
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

        return Optional.of(value.replace(JSONConstants.SPECIAL.toChar(), JSONConstants.COMMA.toChar()).trim());
    }

    public Optional<JSONArray> getJSONArray(String key) {
        final String value = objects.get(key);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JSONArray(value.replace('|', ',')));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<JSONObject> getJSONObject(String key) {
        final String value = objects.get(key);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JSONObject(value.replace('|', ',')));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
