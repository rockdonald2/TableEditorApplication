package edu.ubb.tableeditor.view.table;

import edu.ubb.tableeditor.command.RemoveColumnCommand;
import edu.ubb.tableeditor.command.RemoveRowCommand;
import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.model.Position;
import edu.ubb.tableeditor.utils.PropertiesContext;
import edu.ubb.tableeditor.view.diagrams.BarChartStrategy;
import edu.ubb.tableeditor.view.diagrams.PieChartStrategy;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;
import edu.ubb.tableeditor.view.table.model.SimpleTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class SimpleTable extends JTable implements Table {

    private final JScrollPane container;

    public SimpleTable() {
        int windowWidth = PropertiesContext.getIntProperty("window.size.width");
        int windowHeight = PropertiesContext.getIntProperty("window.size.height");

        this.setModel(this.constructModel(Data.get()));
        this.getTableHeader().setReorderingAllowed(false);

        this.container = new JScrollPane(getTable());
        this.container.setPreferredSize(new Dimension(windowWidth, windowHeight));

        addPopup();
        addEditor();
        addRenderer();
    }

    private void addEditor() {
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject e) {
                if (e instanceof KeyEvent keyEvent) {
                    return startWithKeyEvent(keyEvent);
                }

                return super.isCellEditable(e);
            }

            private boolean startWithKeyEvent(KeyEvent e) {
                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    return false;
                } else return (e.getModifiersEx() & InputEvent.ALT_DOWN_MASK) == 0;
            }
        };

        this.setDefaultEditor(Object.class, editor);
    }

    private void addRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to edit");
        this.setDefaultRenderer(Object.class, renderer);
        this.setShowHorizontalLines(true);
        this.setShowVerticalLines(true);
    }

    private void addPopup() {
        final JPopupMenu popupMenu = new JPopupMenu();

        final JMenuItem deleteRowPopupItem = new JMenuItem("Delete Selected Row");
        deleteRowPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int rowAtPoint = getRowAtPoint(popupMenu);

                if (rowAtPoint > -1) {
                    MainController.instance().executeCommand(new RemoveRowCommand(MainController.instance(), new Position(rowAtPoint, 0)));
                }
            }
        });
        popupMenu.add(deleteRowPopupItem);

        final JMenuItem deleteColPopupItem = new JMenuItem("Delete Selected Column");
        deleteColPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                MainController.instance().executeCommand(new RemoveColumnCommand(MainController.instance(), new Position(0, colAtPoint)));
            }
        });
        popupMenu.add(deleteColPopupItem);

        final JMenuItem sortPopupItem = new JMenuItem("Sort on column values");
        sortPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                MainController.instance().doSort(colAtPoint);
            }
        });
        popupMenu.add(sortPopupItem);

        final JMenu diagramsPopupMenu = new JMenu("Diagrams");
        final JMenuItem pieChartPopupItem = new JMenuItem("Pie chart on row values");
        pieChartPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int rowAtPoint = getRowAtPoint(popupMenu);

                if (rowAtPoint == -1) {
                    return;
                }

                MainController.instance().showDiagram(new PieChartStrategy(), rowAtPoint);
            }
        });
        diagramsPopupMenu.add(pieChartPopupItem);

        final JMenuItem barChartPopupItem = new JMenuItem("Bar chart on column values");
        barChartPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                MainController.instance().showDiagram(new BarChartStrategy(), colAtPoint);
            }
        });
        diagramsPopupMenu.add(barChartPopupItem);

        popupMenu.add(diagramsPopupMenu);

        this.setComponentPopupMenu(popupMenu);
    }

    private int getRowAtPoint(JComponent accordingTo) {
        Point point = SwingUtilities.convertPoint(accordingTo, new Point(0, 0), SimpleTable.this);
        return SimpleTable.this.rowAtPoint(point);
    }

    private int getColumnAtPoint(JComponent accordingTo) {
        Point point = SwingUtilities.convertPoint(accordingTo, new Point(0, 0), SimpleTable.this);
        return SimpleTable.this.columnAtPoint(point);
    }

    @Override
    public JScrollPane getContainer() {
        return this.container;
    }

    @Override
    public JTable getTable() {
        return this;
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        this.setModel(tableModel);
    }

    @Override
    public CustomTableModel constructModel(Data data) {
        return new SimpleTableModel(data);
    }

}
