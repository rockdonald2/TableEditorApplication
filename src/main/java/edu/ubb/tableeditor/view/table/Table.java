package edu.ubb.tableeditor.view.table;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;

import javax.swing.*;

public interface Table {

    JScrollPane getContainer();

    JTable getTable();

    void displayData(CustomTableModel tableModel);

    CustomTableModel defineModel(Data data);

}
