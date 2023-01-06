package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.Position;
import edu.ubb.tableeditor.view.table.Table;

public abstract class ProtectedFieldTableDecorator extends ResettableTableDecorator {

    protected ProtectedFieldTableDecorator(Table table) {
        super(table);
    }

    public abstract Position getProtectedPosition();

}
