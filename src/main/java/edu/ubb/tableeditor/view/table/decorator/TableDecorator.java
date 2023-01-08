package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.view.table.Table;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;

import javax.swing.*;

public abstract class TableDecorator implements Table {

    private final Table table;

    protected TableDecorator(Table table) {
        this.table = table;
    }

    @Override
    public JScrollPane getContainer() {
        return table.getContainer();
    }

    @Override
    public final JTable getTable() {
        return table.getTable();
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        table.displayData(tableModel);
    }

    @Override
    public CustomTableModel defineModel(Data data) {
        return table.defineModel(data);
    }

    public abstract void reset();

}
