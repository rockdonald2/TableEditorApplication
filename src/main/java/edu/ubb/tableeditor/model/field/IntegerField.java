package edu.ubb.tableeditor.model.field;

import edu.ubb.tableeditor.service.export.ExportVisitor;

public class IntegerField extends Field {

    private int value;

    public IntegerField(String key, int value) {
        super(key);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String accept(ExportVisitor visitor) {
        return visitor.visit(this);
    }

}
