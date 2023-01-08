package edu.ubb.tableeditor.utils.json;

public enum JsonConstants {

    CURLY_OPEN_BRACKETS('{'),
    CURLY_CLOSE_BRACKETS('}'),
    SQUARE_OPEN_BRACKETS('['),
    SQUARE_CLOSE_BRACKETS(']'),
    COLON(':'),
    COMMA(','),
    SPECIAL('|'),
    SINGLE_QUOTE('\''),
    DOUBLE_QUOTE('"');

    private final char constant;

    JsonConstants(char constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return String.valueOf(this.constant);
    }

    public char toChar() {
        return this.constant;
    }

}
