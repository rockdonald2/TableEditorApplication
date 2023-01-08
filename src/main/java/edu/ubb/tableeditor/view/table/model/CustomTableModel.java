package edu.ubb.tableeditor.view.table.model;

import edu.ubb.tableeditor.model.Data;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

public abstract class CustomTableModel extends DefaultTableModel {

    protected CustomTableModel() {
    }

    protected CustomTableModel(Data data) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        String[][] tableData = new String[rowData.size()][headers.size()];
        IntStream.range(0, rowData.size())
                .forEach(idx -> tableData[idx] = rowData.get(idx).toArray(new String[]{}));

        this.setDataVector(tableData, headers.toArray(new String[]{}));
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
