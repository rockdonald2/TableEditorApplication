package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.view.table.Table;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;
import edu.ubb.tableeditor.view.table.model.RestrictedTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class ValueRestrictedTableDecorator extends TableDecorator {

    public ValueRestrictedTableDecorator(Table table) {
        super(table);
    }

    @Override
    public void reset() {
        // do nothing, as it is feature flag
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        super.displayData(tableModel);

        tableModel.getValueRestrictions()
                .forEach(restriction -> {
                    final TableColumn tableColumn = this.getTable().getColumn(restriction.getKey());
                    final JComboBox<String> comboBox = new JComboBox<>();
                    restriction.getValue().forEach(comboBox::addItem);
                    tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
                });
    }

    @Override
    public CustomTableModel defineModel(Data data) {
        return new RestrictedTableModel(data);
    }

}
