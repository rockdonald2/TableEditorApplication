package edu.ubb.tableeditor.view.table.model.decorator;

import com.ezylang.evalex.BaseException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;
import edu.ubb.tableeditor.view.MainPanel;
import edu.ubb.tableeditor.view.table.model.CustomTableModel;
import edu.ubb.tableeditor.view.table.model.decorator.functions.CountFunction;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaCapableTableModel extends TableModelDecorator {

    final ExpressionConfiguration configuration;

    public FormulaCapableTableModel(CustomTableModel tableModel) {
        super(tableModel);

        configuration = ExpressionConfiguration.builder()
                .allowOverwriteConstants(true)
                .arraysAllowed(false)
                .implicitMultiplicationAllowed(true)
                .stripTrailingZeros(true)
                .structuresAllowed(false)
                .build()
                .withAdditionalFunctions(Map.entry("COUNT", new CountFunction()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        AtomicReference<String> exprString = new AtomicReference<>(aValue.toString().trim());

        if (!exprString.get().startsWith("=") && !exprString.get().startsWith("-") && !exprString.get().startsWith("+")) {
            super.setValueAt(aValue, rowIndex, columnIndex);
            return;
        }

        if (exprString.get().startsWith("=")) {
            exprString.set(exprString.get().substring(1));
        }

        final List<String> columns = new ArrayList<String>(this.getColumnIdentifiers());
        final int rowsCount = this.getRowCount();

        Expression expr = new Expression(exprString.get(), configuration);

        Set<String> variables;
        try {
            variables = expr.getUndefinedVariables();
        } catch (ParseException e) {
            MainPanel.instance().showError("Failed to parse expression: " + exprString + " (" + e.getMessage() + ")");
            return;
        }

        Map<String, Object> varDefs = new HashMap<>();

        try {
            variables.forEach(variable -> {
                columns.forEach(column -> {
                    if (Pattern.compile("^" + column + "\\d+$").matcher(variable).matches()) {
                        Matcher idxMatcher = Pattern.compile("\\d+$").matcher(variable);

                        if (idxMatcher.find()) {
                            int foundRowIdx = Integer.parseInt(idxMatcher.group(0));

                            if (foundRowIdx <= rowsCount) {
                                varDefs.put(variable, Double.parseDouble(this.getValueAt(foundRowIdx - 1, columns.indexOf(column)).toString()));
                            }
                        }
                    }
                });
            });
        } catch (NumberFormatException | NullPointerException e) {
            MainPanel.instance().showError("Failed to parse expression: " + exprString + " (" + e.getMessage() + " is not a number or null)");
            return;
        }

        expr = expr.withValues(varDefs);

        String evaluated;
        try {
            evaluated = expr.evaluate().getStringValue();
        } catch (BaseException e) {
            MainPanel.instance().showError("Failed to parse expression: " + exprString + " (" + e.getMessage() + ")");
            return;
        }

        super.setValueAt(evaluated, rowIndex, columnIndex);
    }

}
