package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.view.table.Table;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RowNumberTableDecorator extends TableDecorator {

    public RowNumberTableDecorator(Table table) {
        super(table);

        final JScrollPane container = table.getContainer();
        final RowNumberTable numberedTable = new RowNumberTable(table.getTable());

        container.setRowHeaderView(numberedTable);
        container.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, numberedTable.getTableHeader());
    }

    @Override
    public void reset() {
        final JScrollPane container = super.getContainer();
        container.setRowHeaderView(null);
        container.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, null);
    }

    public static class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener {

        private final JTable innerTable;

        public RowNumberTable(JTable innerTable) {
            this.innerTable = innerTable;
            this.innerTable.addPropertyChangeListener(this);
            this.innerTable.getModel().addTableModelListener(this);

            this.setFocusable(false);
            this.setAutoCreateColumnsFromModel(false);
            this.setSelectionModel(this.innerTable.getSelectionModel());

            this.getTableHeader().setReorderingAllowed(false);

            TableColumn column = new TableColumn();
            column.setHeaderValue(" ");
            this.addColumn(column);
            column.setCellRenderer(new RowNumberRenderer());

            this.getColumnModel().getColumn(0).setPreferredWidth(50);
            this.setPreferredScrollableViewportSize(getPreferredSize());
        }

        @Override
        public void addNotify() {
            super.addNotify();

            Component c = getParent();
            //  Keep scrolling of the row table in sync with the main table.
            if (c instanceof JViewport viewport) {
                viewport.addChangeListener(this);
            }
        }

        @Override
        public int getRowCount() {
            return innerTable.getRowCount();
        }

        @Override
        public int getRowHeight(int row) {
            int rowHeight = innerTable.getRowHeight(row);

            if (rowHeight != super.getRowHeight(row)) {
                super.setRowHeight(row, rowHeight);
            }

            return rowHeight;
        }

        @Override
        public Object getValueAt(int row, int column) {
            return Integer.toString(row + 1);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void stateChanged(ChangeEvent e) {
            JViewport viewport = (JViewport) e.getSource();
            JScrollPane scrollPane = (JScrollPane) viewport.getParent();
            scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
        }

        public void propertyChange(PropertyChangeEvent e) {
            if ("selectionModel".equals(e.getPropertyName())) {
                setSelectionModel(innerTable.getSelectionModel());
            }

            if ("rowHeight".equals(e.getPropertyName())) {
                repaint();
            }

            if ("model".equals(e.getPropertyName())) {
                innerTable.getModel().addTableModelListener(this);
                revalidate();
            }
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            revalidate();
        }

        private static class RowNumberRenderer extends DefaultTableCellRenderer {
            public RowNumberRenderer() {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (table != null) {
                    JTableHeader header = table.getTableHeader();

                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                }

                if (isSelected) {
                    setFont(getFont().deriveFont(Font.BOLD));
                }

                setText((value == null) ? "" : value.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));

                return this;
            }
        }
    }

}
