package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.view.table.Table;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;
import edu.ubb.tableeditor.view.table.model.decorator.FormulaCapableTableModel;

public class FormulaCapableTableDecorator extends TableDecorator {


    public FormulaCapableTableDecorator(Table table) {
        super(table);
    }

    @Override
    public void reset() {
        // do nothing as there is nothing to reset
    }

    @Override
    public CustomTableModel constructModel(Data data) {
        return new FormulaCapableTableModel(super.constructModel(data));
    }

}
