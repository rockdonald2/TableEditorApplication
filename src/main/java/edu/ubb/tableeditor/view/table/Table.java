package edu.ubb.tableeditor.view.table;

import edu.ubb.tableeditor.view.table.model.CustomTableModel;

import javax.swing.*;

public interface Table {

    JTable getComponent();

    void displayData(CustomTableModel tableModel);

    CustomTableModel constructModel(String[][] data, String[] headers);

}
