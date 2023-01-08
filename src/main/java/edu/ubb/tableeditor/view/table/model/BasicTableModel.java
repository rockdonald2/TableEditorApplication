package edu.ubb.tableeditor.view.table.model;

import edu.ubb.tableeditor.command.EditCommand;
import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.model.field.Position;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicTableModel extends CustomTableModel {

    public BasicTableModel(Data data) {
        super(data);
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

    @Override
    public List<Map.Entry<String, List<String>>> getValueRestrictions() {
        return Collections.emptyList();
    }

    @Override
    public void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions) {
        // do nothing
    }

}
