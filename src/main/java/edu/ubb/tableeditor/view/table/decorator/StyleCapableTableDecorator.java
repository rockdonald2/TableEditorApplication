package edu.ubb.tableeditor.view.table.decorator;

import edu.ubb.tableeditor.model.field.Position;
import edu.ubb.tableeditor.view.table.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StyleCapableTableDecorator extends TableDecorator {

    private final JScrollPane container;
    private Map<Position, List<Style>> styles = new HashMap<>();

    public StyleCapableTableDecorator(Table table) {
        super(table);

        table.getTable().setDefaultRenderer(Object.class, new StyledTableCellRenderer(styles));

        final JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(table.getContainer());

        final JToolBar toolbar = new JToolBar();

        final JButton boldBtn = new JButton("B");
        boldBtn.setFont(boldBtn.getFont().deriveFont(Font.BOLD));
        final JButton italicBtn = new JButton("I");
        italicBtn.setFont(italicBtn.getFont().deriveFont(Font.ITALIC));

        boldBtn.addActionListener(e -> reevaluateStyles(Style.BOLD));
        italicBtn.addActionListener(e -> reevaluateStyles(Style.ITALIC));

        toolbar.add(boldBtn);
        toolbar.add(italicBtn);

        container = new JScrollPane(wrapperPanel);
        container.setColumnHeaderView(toolbar);
    }

    private void reevaluateStyles(Style style) {
        final List<Integer> selectedRows = Arrays.stream(this.getTable().getSelectedRows()).boxed().toList();
        final List<Integer> selectedColumns = Arrays.stream(this.getTable().getSelectedColumns()).boxed().toList();

        for (Integer selectedColumn : selectedColumns) {
            for (Integer selectedRow : selectedRows) {
                final Position key = new Position(selectedRow, selectedColumn);

                // that position has some styles
                if (styles.containsKey(key)) {
                    final List<Style> appliedStyles = styles.get(key);

                    // if the style already applied, remove it
                    if (appliedStyles.stream().anyMatch(s -> s.equals(style))) {
                        styles.put(key, new ArrayList<>(appliedStyles.stream().filter(s -> !s.equals(style)).toList()));
                    } else {
                        // set it
                        styles.get(key).add(style);
                    }
                } else {
                    // if no style is applied
                    styles.put(key, new ArrayList<>(List.of(style)));
                }
            }
        }

        this.getTable().repaint();
        this.getTable().validate();
    }

    @Override
    public void reset() {
        container.setColumnHeaderView(null);
    }

    @Override
    public JScrollPane getContainer() {
        return container;
    }

    public Map<Position, List<Style>> getStyles() {
        return styles;
    }

    public void setStyles(Map<Position, List<Style>> styles) {
        this.styles = styles;
        this.getTable().setDefaultRenderer(Object.class, new StyledTableCellRenderer(styles));
    }

    public enum Style {
        BOLD,
        ITALIC
    }

    private static class StyledTableCellRenderer extends DefaultTableCellRenderer {

        private final Map<Position, List<Style>> styles;

        public StyledTableCellRenderer(Map<Position, List<Style>> styles) {
            this.styles = styles;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            final Position pos = new Position(row, column);
            List<Style> appliedStyles = styles.get(pos);

            if (appliedStyles == null) {
                appliedStyles = Collections.emptyList();
            }

            AtomicInteger newStyle = new AtomicInteger(Font.PLAIN);
            appliedStyles.forEach(style -> {
                if (style.equals(Style.BOLD)) {
                    newStyle.updateAndGet(v -> v | Font.BOLD);
                } else if (style.equals(Style.ITALIC)) {
                    newStyle.updateAndGet(v -> v | Font.ITALIC);
                }
            });

            c.setFont(c.getFont().deriveFont(newStyle.get()));

            return c;
        }

    }

}
