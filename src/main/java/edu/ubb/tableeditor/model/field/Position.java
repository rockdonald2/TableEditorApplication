package edu.ubb.tableeditor.model.field;

import java.util.Objects;

public class Position {

    public static final String SEPARATOR = ";";

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Position of(String value) {
        String[] splitted = value.split(SEPARATOR);

        if (splitted.length != 2) {
            return null;
        }

        try {
            return new Position(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

}
