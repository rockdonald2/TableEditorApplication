package edu.ubb.tableeditor.utils.json;

import java.util.*;

public class JSONArray extends JSONObject {

    private ArrayList<String> objects;

    public JSONArray(String arg) {
        super();
        Objects.requireNonNull(arg);

        arg = arg.trim();

        if (!arg.startsWith(JSONConstants.SQUARE_OPEN_BRACKETS.toString()) && !arg.endsWith(JSONConstants.SQUARE_CLOSE_BRACKETS.toString())) {
            throw new IllegalArgumentException("Failed to parse input string, not a valid JSONArray");
        }

        getJSONObjects(arg.trim());
    }

    @Override
    protected void getJSONObjects(String arg) {
        objects = new ArrayList<>();

        if (arg.startsWith(String.valueOf(JSONConstants.SQUARE_OPEN_BRACKETS)) && arg.endsWith(String.valueOf(JSONConstants.SQUARE_CLOSE_BRACKETS))) {
            StringBuilder builder = new StringBuilder(arg);

            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);

            innerReplaces(builder);

            Collections.addAll(objects, Arrays.stream(builder.toString().split(String.valueOf(JSONConstants.COMMA))).map(String::trim).toList().toArray(new String[0]));
        }
    }

    @Override
    protected void innerReplaces(StringBuilder arg) {
        boolean isArray = false;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);
            if (isArray && String.valueOf(a).compareTo(String.valueOf(JSONConstants.COMMA)) == 0) {
                arg.setCharAt(i, JSONConstants.SPECIAL.toChar());
            }

            if (String.valueOf(a).compareTo(String.valueOf(JSONConstants.CURLY_OPEN_BRACKETS)) == 0) {
                isArray = true;
            }

            if (String.valueOf(a).compareTo(String.valueOf(JSONConstants.CURLY_CLOSE_BRACKETS)) == 0) {
                isArray = false;
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
    public Optional<JSONArray> getJSONArray(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<JSONObject> getJSONObject(String key) {
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

        return Optional.of(value.replace(JSONConstants.SPECIAL.toChar(), JSONConstants.COMMA.toChar()).trim());
    }

    public Optional<JSONArray> getJSONArray(int index) {
        final String value = objects.get(index);

        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new JSONArray(value.replace('|', ',')));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<JSONObject> getJSONObject(int index) {
        final String value = objects.get(index);

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
