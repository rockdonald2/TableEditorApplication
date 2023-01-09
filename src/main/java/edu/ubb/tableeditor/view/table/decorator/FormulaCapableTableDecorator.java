package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.view.table.Table;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;
import edu.ubb.tableeditor.view.table.model.decorator.FormulaCapableTableModel;

public class FormulaCapableTableDecorator extends TableDecorator {


    public FormulaCapableTableDecorator(Table table) {
        super(table);
    }

    @Override
    public CustomTableModel defineModel(Data data) {
        return new FormulaCapableTableModel(super.defineModel(data));
    }

}
