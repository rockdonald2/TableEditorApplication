package edu.ubb.tableeditor.view.table.model.decorator;

import edu.ubb.tableeditor.view.table.model.CustomTableModel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class TableModelDecorator extends CustomTableModel {

    private final CustomTableModel tableModel;

    protected TableModelDecorator(CustomTableModel tableModel) {
        this.tableModel = tableModel;
        this.setDataVector(getDataVector(), getColumnIdentifiers());
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.tableModel.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.tableModel.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.tableModel.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Vector<Vector> getDataVector() {
        return this.tableModel.getDataVector();
    }

    @Override
    public Vector getColumnIdentifiers() {
        return this.tableModel.getColumnIdentifiers();
    }

    @Override
    public List<Map.Entry<String, List<String>>> getValueRestrictions() {
        return this.tableModel.getValueRestrictions();
    }

    @Override
    public void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions) {
        this.tableModel.setValueRestrictions(valueRestrictions);
    }

}
