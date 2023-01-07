package edu.ubb.tableeditor.view.table.model;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public abstract class CustomTableModel extends DefaultTableModel {

    protected CustomTableModel() {
    }

    protected CustomTableModel(String[][] data, String[] headers) {
        super(data, headers);
        this.setDataVector(data, headers);
    }

    @Override
    public abstract Object getValueAt(int row, int column);

    @Override
    public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);

    @Override
    public abstract boolean isCellEditable(int rowIndex, int columnIndex);

    @Override
    public Vector<Vector> getDataVector() {
        return super.getDataVector();
    }

    public Vector getColumnIdentifiers() {
        return super.columnIdentifiers;
    }

}
