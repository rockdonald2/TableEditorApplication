package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.view.table.Table;

public abstract class ResettableTableDecorator extends TableDecorator {

    protected ResettableTableDecorator(Table table) {
        super(table);
    }

    public abstract void resetModel();

}
