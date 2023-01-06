package edu.ubb.tableeditor.view.table.model;

import edu.ubb.tableeditor.command.EditCommand;
import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.Position;

public class SimpleTableModel extends CustomTableModel {

    public SimpleTableModel(String[][] data, String[] headers) {
        super(data, headers);
    }

    @Override
    public Object getValueAt(int row, int column) {
        return MainController.instance().getValueAt(new Position(row, column));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        MainController.instance().executeCommand(new EditCommand(MainController.instance(), aValue.toString(), new Position(rowIndex, columnIndex)));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
